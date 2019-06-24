package util;

import com.alibaba.druid.pool.DruidDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DruidManager {
    private DruidManager() {
    }

    private static DruidManager single = null;
    private DruidDataSource dataSource;

    public synchronized static DruidManager getInstance() {
        if (single == null) {
            single = new DruidManager();
            single.initPool();
        }
        return single;
    }

    private void initPool() {
        dataSource = new DruidDataSource();
        dataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
        dataSource.setUsername("wubeibei");
        dataSource.setPassword("123456");
        dataSource.setUrl("JDBC:oracle:thin:@localhost:1521/orclpdb");
        dataSource.setInitialSize(5);
        dataSource.setMinIdle(1);
        dataSource.setMaxActive(10);
        // 启用监控统计功能
        try {
            dataSource.setFilters("stat");
        } catch (SQLException e) {
            throw new ExceptionInInitializerError(e);
        }// for mysql
        dataSource.setPoolPreparedStatements(false);
    }

    //要考虑多线程的情况
    public Connection getConnection() throws Exception {
        Connection connection = null;
        try {
            synchronized (dataSource) {
                connection = dataSource.getConnection();
            }
        } catch (SQLException e) {
            throw new Exception(e);
        }
        return connection;
    }
}