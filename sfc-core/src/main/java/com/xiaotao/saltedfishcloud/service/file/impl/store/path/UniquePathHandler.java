package com.xiaotao.saltedfishcloud.service.file.impl.store.path;

import com.xiaotao.saltedfishcloud.service.file.impl.store.LocalStoreConfig;
import com.xiaotao.saltedfishcloud.exception.JsonException;
import com.xiaotao.saltedfishcloud.entity.po.file.BasicFileInfo;
import org.springframework.stereotype.Component;

/**
 * 利用文件MD5获得文件路径，保证一个内容相同的文件存储的路径是唯一的且不受文件名影响。
 */
@Component
public class UniquePathHandler implements PathHandler {
    /**
     * 若FileInfo是文件夹，则返回null。公共用户会使用RawPathHandler
     * @param uid       用户ID 0表示公共
     * @param targetDir 请求的目标目录（是相对用户网盘根目录的目录）
     * @param fileInfo  新文件信息
     * @return 路径
     */
    @Override
    public String getStorePath(int uid, String targetDir, BasicFileInfo fileInfo) {
        if (fileInfo.isDir()) {
            return null;
        } else {
            if (fileInfo.getMd5() == null || fileInfo.getMd5().length() != 32) {
                throw new JsonException(500, "无效MD5");
            }
            String md5 = fileInfo.getMd5();
            return LocalStoreConfig.getUniqueStoreRoot()
                    + "/"
                    + md5.substring(0,2)
                    + "/"
                    + md5.substring(2, 4)
                    + "/"
                    + md5;
        }
    }
}
