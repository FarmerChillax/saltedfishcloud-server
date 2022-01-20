package com.xiaotao.saltedfishcloud.dao.mybatis;

import com.xiaotao.saltedfishcloud.annotations.NullToZero;
import org.apache.ibatis.annotations.Select;

@NullToZero
public interface FileAnalyseDao {

    /**
     * 取用户数据总大小
     */
    @Select("SELECT SUM(size) FROM file_table WHERE size != -1 AND uid != 0")
    Long getUserTotalSize();

    /**
     * 取用户数据总大小
     */
    @Select("SELECT SUM(size) FROM file_table WHERE size != -1 AND uid = 0")
    Long getPublicTotalSize();

    /**
     * 取唯一存储下用户实际数据大小
     */
    @Select("SELECT SUM(size) FROM (SELECT ANY_VALUE(size) size FROM file_table WHERE size != -1 AND uid != 0 GROUP BY md5) AS temp")
    Long getRealTotalUserSize();

    /**
     * 取目录数
     */
    @Select("SELECT count(*) FROM file_table WHERE size = -1")
    Long getDirCount();

    /**
     * 取文件数
     */
    @Select("SELECT count(*) FROM file_table WHERE size != -1")
    Long getFileCount();

    @Select("SELECT SUM(size) FROM file_table WHERE size != -1 AND uid = 123123")
    Long test();
}
