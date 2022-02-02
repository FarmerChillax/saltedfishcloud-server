package com.xiaotao.saltedfishcloud.entity.po.file;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.core.io.InputStreamSource;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

/**
 * @TODO 本地文件，网盘文件，临时文件，数据库文件记录混合使用，需单独拆分
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class FileInfo extends BasicFileInfo{
    /**
     * 文件所属用户ID
     */
    private Integer uid;
    private String parent;

    /**
     * 相对用户网盘中，文件所在的目录路径，或在存储系统中的完整路径
     */
    @JsonIgnore
    private String path;
    private String node;
    private Long lastModified;
    private Date createdAt;
    private Date updatedAt;

    @JsonIgnore
    private InputStreamSource streamSource;

    public static FileInfo getFromResource(Resource resource, Integer uid, Integer type) throws IOException {
        final FileInfo fileInfo = new FileInfo();
        Date now = new Date();
        fileInfo.setName(resource.getFilename());
        fileInfo.setUid(uid);
        fileInfo.setCreatedAt(now);
        fileInfo.setSize(resource.contentLength());
        fileInfo.setLastModified(now.getTime());
        fileInfo.setType(type);
        return fileInfo;
    }

    /**
     * 获取本地文件的文件信息（将自动计算其MD5）
     * @param path  本地文件路径
     * @return  文件信息对象
     */
    public static FileInfo getLocal(String path) {
        try {
            return getLocal(path, true);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取本地文件的文件信息
     * @param path  本地文件路径
     * @param computeMd5 是否计算MD5
     * @return  文件信息对象
     */
    public static FileInfo getLocal(String path, boolean computeMd5) throws IOException {
        FileInfo info = new FileInfo(new File(path));
        if (computeMd5) {
            info.updateMd5();
        }
        return info;
    }

    public FileInfo(MultipartFile file) {
        this(file.getName(), file.getSize(), TYPE_FILE, null, System.currentTimeMillis(), file);
    }

    public FileInfo(File file) {
        this(file.getName(), file.isDirectory() ? -1 : file.length(), file.isDirectory() ? TYPE_DIR : TYPE_FILE, file.getPath(), file.lastModified(), new PathResource(file.getPath()));
    }

    public FileInfo(String name, long size, int type, String path, long lastModified, InputStreamSource streamSource) {
        this.name = name;
        this.size = size;
        this.type = type;
        this.path = path;
        this.lastModified = lastModified;
        this.streamSource = streamSource;
    }

    /**
     * 更新MD5值，仅当对象通过有参构造方法创建才有效
     */
    public void updateMd5() throws IOException {
        if (isDir()) return;
        if (md5 == null) {
            try(final InputStream is = streamSource.getInputStream()) {
                md5 = DigestUtils.md5DigestAsHex(is);
            }
        }
    }
}
