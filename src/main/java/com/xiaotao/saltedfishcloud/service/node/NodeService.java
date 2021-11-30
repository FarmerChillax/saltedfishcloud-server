package com.xiaotao.saltedfishcloud.service.node;

import com.xiaotao.saltedfishcloud.dao.mybatis.NodeDao;
import com.xiaotao.saltedfishcloud.entity.po.NodeInfo;
import com.xiaotao.saltedfishcloud.exception.JsonException;
import com.xiaotao.saltedfishcloud.helper.PathBuilder;
import com.xiaotao.saltedfishcloud.service.node.cache.NodeCacheService;
import com.xiaotao.saltedfishcloud.service.node.cache.annotation.RemoveNodeCache;
import com.xiaotao.saltedfishcloud.utils.SecureUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.NoSuchFileException;
import java.util.*;

/**
 * 节点服务，用于管理目录存储节点相关信息
 * 缓存key：
 * path::{uid}:node:{nid}                   通过节点ID查询节点信息（NodeInfo）的缓存
 * path::{uid}:pnid:{parentId}:{nodeName}   通过父节点ID和节点名称查询节点信息（NodeInfo）的缓存
 * path::{uid}:path:{nodeId}                通过节点ID查询路径
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class NodeService {
    private final NodeDao nodeDao;
    @Autowired
    private NodeCacheService cacheService;
    @Autowired
    private NodeService self;

    /**
     * 根据用户ID和节点ID获取对应的节点信息，可识别到根ID
     * @param uid   用户ID
     * @param nid   节点ID，可以是根ID
     * @return      节点ID，若无结果则为null
     */
    @Cacheable(cacheNames = "path", key = "#uid+':node:'+#nid")
    public NodeInfo getNodeById(Integer uid, String nid) {
        if (nid.length() == 32) {
            return nodeDao.getNodeById(uid, nid);
        }
        NodeInfo node = new NodeInfo();
        node.setId(uid + "");
        node.setUid(uid);
        return node;
    }

    public NodeTree getFullTree(int uid) {
        NodeTree tree = new NodeTree();
        List<NodeInfo> allNode = nodeDao.getAllNode(uid);
        tree.putNode(NodeInfo.getRootNode(uid));
        if (allNode != null) {
            allNode.forEach(tree::putNode);
        }
        return tree;
    }

    @Cacheable(cacheNames = "path", key = "#uid+':pnid:'+#parentId+':'+#nodeName")
    public NodeInfo getNodeByParentId(int uid, String parentId, String nodeName) {
        return nodeDao.getNodeByParentId(uid, parentId, nodeName);
    }

    /**
     * 获取某个路径中途径的节点信息
     * @param uid   用户ID
     * @param path  路径
     * @throws NoSuchFileException 请求的路径不存在时抛出此异常
     * @return  节点信息列表
     */
    public LinkedList<NodeInfo> getPathNodeByPath(int uid, String path) throws NoSuchFileException {
        log.debug("search path" + uid + ": " + path);
        LinkedList<NodeInfo> link = new LinkedList<>();
        PathBuilder pb = new PathBuilder();
        pb.append(path);
        Collection<String> paths = pb.getPath();
        Set<String> visited = new HashSet<>();

        String strId = "" + uid;
        try {
            for (String node : paths) {
                String parent = link.isEmpty() ? strId : link.getLast().getId();
                NodeInfo info = self.getNodeByParentId(uid, parent, node);
                if (info == null) {
                    throw new NoSuchFileException("路径 " + path + " 不存在，或目标节点信息已丢失");
                }
                if (visited.contains(info.getId())) {
                    throw new JsonException(500, "出现文件夹循环包含，请联系管理员并提供以下信息：uid=" + uid + " " + info.getId() + " => " + node);
                } else {
                    visited.add(node);
                    link.add(info);
                }
            }
            if (link.isEmpty()) {
                throw new NullPointerException();
            }
        } catch (NullPointerException e) {
            NodeInfo info = new NodeInfo();
            info.setId(strId);
            info.setUid(uid);
            link.add(info);
        }
        if (log.isDebugEnabled()) {
            StringBuilder sb = new StringBuilder();
            for (NodeInfo nodeInfo : link) {
                log.debug("nodeInfo:" + nodeInfo);
                sb.append("/").append(nodeInfo.getName() == null ? "" : nodeInfo.getName()).append('[').append(nodeInfo.getId()).append(']');
            }
            log.debug(sb.toString());
        }
        return link;
    }

    /**
     * 添加一个节点
     * @param uid 用户ID
     * @param name 名称
     * @param parent 父节点ID
     * @return 新节点ID
     */
    public String addNode(int uid, String name, String parent) {
        int i;
        String id;
        cacheService.deletePnidCache(uid, parent, name);
        NodeInfo node = nodeDao.getNodeByParentId(uid, parent, name);
        if (node != null) {
            return node.getId();
        } else {
            do {
                id = SecureUtils.getUUID();
                i = nodeDao.addNode(uid, name, id, parent);
            } while (i == 0);
            return id;
        }
    }

    /**
     * 取某节点下的所有子节点
     * @param uid 用户ID
     * @param nid 目标节点ID
     * @return 目标节点下的所有子节点（不包含自己）
     */
    public List<NodeInfo> getChildNodes(int uid, String nid) {
        // 最终结果
        List<NodeInfo> res = new LinkedList<>();

        // 单次查询得到的节点集合
        List<NodeInfo> nodes;

        // nodes中的id部分
        List<String> ids = new LinkedList<>();
        ids.add(nid);
        do {
            nodes = nodeDao.getChildNodes(uid, ids);
            res.addAll(nodes);
            ids.clear();
            nodes.forEach(nodeInfo -> ids.add(nodeInfo.getId()));
        } while (!nodes.isEmpty());
        return res;
    }


    /**
     * 移除节点
     * @param uid   用户ID
     * @param ids   节点ID集合
     * @return  删除数
     */
    @RemoveNodeCache(uid = 0, nid = 1)
    public int deleteNodes(int uid, Collection<String> ids) {
        if (!ids.isEmpty()) {
            return nodeDao.deleteNodes(uid, ids);
        } else {
            return 0;
        }
    }

    /**
     * 获取路径对应的节点ID
     * @param uid   用户ID
     * @param path  请求的路径
     * @return  节点ID
     * @throws NoSuchFileException  路径不存在
     */
    public String getNodeIdByPath(int uid, String path) throws NoSuchFileException {
        return self.getPathNodeByPath(uid, path).getLast().getId();
    }

    /**
     * 通过节点ID 获取节点所在的完整路径位置
     * @param uid       用户ID
     * @param nodeId    节点ID
     * @return          完整路径
     */
    @Cacheable(cacheNames = "path", key = "#uid+':path:'+#nodeId")
    public String getPathByNode(int uid, String nodeId) {
        if (nodeId.length() < 32) {
            return "/";
        }
        LinkedList<String> link = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        String lastId = nodeId;
        NodeInfo info;
        visited.add(nodeId);

        // 迭代查询
        while ( (info =  self.getNodeById(uid, lastId)) != null) {
            link.addFirst(info.getName());
            lastId = info.getParent();
            if (visited.contains(lastId)) {
                throw new JsonException(500, "出现文件夹循环包含，请联系管理员并提供以下信息：uid=" + uid + " " + info.getId() + " => " + lastId);
            }
            if (info.getParent().length() < 32) {
                break;
            }
        }
        if (link.isEmpty()) {
            throw new JsonException(404, "无效的nodeId");
        }
        StringBuilder stringBuilder = new StringBuilder();
        link.forEach(name -> stringBuilder.append("/").append(name));
        return stringBuilder.toString();
    }
}
