package com.xiaotao.saltedfishcloud.compress.filesystem;

import com.xiaotao.saltedfishcloud.compress.impl.CompressFileImpl;
import org.apache.commons.compress.archivers.ArchiveEntry;

public interface CompressFile {

    /**
     * 获取文件名
     * @TODO 识别编码不一致导致的文件名乱码还原
     */
    String getName();

    /**
     * 判断是否为目录
     */
    boolean isDirectory();

    /**
     * 获取文件大小
     */
    long getSize();

    /**
     * 获取文件在压缩文件中的完整路径
     */
    String getPath();

    static CompressFile forArchiveEntry(ArchiveEntry entry) {
        if (entry == null) throw new NullPointerException();
        return new CompressFileImpl(entry);
    }
}
