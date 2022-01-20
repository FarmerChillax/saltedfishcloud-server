package com.xiaotao.saltedfishcloud.init;

import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Component
@Order(1)
@Slf4j
public class DatabaseInitializer implements ApplicationRunner {
    @Resource
    private DataSource dataSource;

    /**
     * 判断数据库中是否存在数据表<br>
     * 执行后数据库连接不会被关闭
     * @author xiaotao mjt233@qq.com
     * @param conn  数据库连接
     */
    private boolean isTableExist(Connection conn) throws SQLException {
        var stat = conn.createStatement();
        // 获取当前数据库名
        var res = stat.executeQuery("SELECT database() AS db_name");
        res.next();
        var dbName = res.getString("db_name");

        // 获取当前数据库中的所有数据表
        res = stat.executeQuery("SELECT table_name FROM information_schema.columns WHERE table_schema = '" + dbName + "' GROUP BY table_name");
        boolean ret = res.next();
        res.close();
        return ret;
    }

    /**
     * SpringBoot启动时执行的方法（ApplicationRunner接口）
     * @author xiaotao mjt233@qq.com
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        var con = dataSource.getConnection();

        // 若数据库无数据表则先初始化
        if (!isTableExist(con)) {
            log.info("[数据库]正在初始化数据表...");
            var resource = new ClassPathResource("sql/full.sql");
            ScriptUtils.executeSqlScript(con, resource);
            log.info("[数据库]数据表初始化完成（好耶）");
        }
        con.close();

    }
}
