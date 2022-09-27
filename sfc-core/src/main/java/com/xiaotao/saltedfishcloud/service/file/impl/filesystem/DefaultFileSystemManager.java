package com.xiaotao.saltedfishcloud.service.file.impl.filesystem;

import com.xiaotao.saltedfishcloud.exception.FileSystemParameterException;
import com.xiaotao.saltedfishcloud.exception.UnsupportedFileSystemProtocolException;
import com.xiaotao.saltedfishcloud.service.file.DiskFileSystem;
import com.xiaotao.saltedfishcloud.service.file.DiskFileSystemFactory;
import com.xiaotao.saltedfishcloud.service.file.DiskFileSystemManager;
import com.xiaotao.saltedfishcloud.service.mountpoint.MountPointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class DefaultFileSystemManager implements DiskFileSystemManager {
    /**
     * 记录各个文件系统对应的所支持的协议
     */
    private final Map<String, DiskFileSystemFactory> factoryMap = new ConcurrentHashMap<>();

    @Autowired
    private MountPointService mountPointService;

    private DiskFileSystemDispatcher dispatcher;

    @Override
    public List<DiskFileSystemFactory> listAllFileSystem() {
        return List.copyOf(factoryMap.values());
    }


    @Override
    public DiskFileSystem getMainFileSystem() {
        return dispatcher;
    }

    @Override
    public void setMainFileSystem(DiskFileSystem fileSystem) {
        this.dispatcher = new DiskFileSystemDispatcher(fileSystem, mountPointService, this);
    }

    @Override
    public void registerFileSystem(DiskFileSystemFactory factory) {
        DiskFileSystemFactory existFactory = factoryMap.get(factory.getDescribe().getProtocol().toLowerCase());
        if (existFactory != null) {
            throw new IllegalArgumentException(factory.getDescribe().getProtocol() + "协议的文件系统已经被注册：" + factory.getDescribe());
        } else {
            factoryMap.put(factory.getDescribe().getProtocol(), factory);
        }
    }


    @Override
    public DiskFileSystem getFileSystem(String protocol, Map<String, Object> params) throws FileSystemParameterException {
        DiskFileSystemFactory factory = factoryMap.get(protocol);
        if (factory == null) {
            throw new UnsupportedFileSystemProtocolException(protocol);
        }
        return factory.getFileSystem(params);
    }

    @Override
    public boolean isSupportedProtocol(String protocol) {
        return factoryMap.containsKey(protocol.toLowerCase());
    }

    @Override
    public List<DiskFileSystemFactory> listPublicFileSystem() {
        return factoryMap.values().stream().filter(e -> e.getDescribe().isPublic()).collect(Collectors.toList());
    }
}
