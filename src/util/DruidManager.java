package util;

import com.alibaba.druid.pool.DruidDataSource;
import oracle.jdbc.pool.OracleDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
        dataSource.setPassword("a690252189");
        dataSource.setUrl("JDBC:oracle:thin:@localhost:1521/orclpdb");
        dataSource.setInitialSize(5);
        dataSource.setMinIdle(1);
        dataSource.setMaxActive(10);
        dataSource.setTimeBetweenEvictionRunsMillis(20000);
        dataSource.setRemoveAbandoned(true);
        dataSource.setRemoveAbandonedTimeout(30);
        dataSource.setTestWhileIdle(true);
        dataSource.setTestOnBorrow(true);
//        // 启用监控统计功能
//        try {
//            dataSource.setFilters("stat");
//        } catch (SQLException e) {
//            throw new ExceptionInInitializerError(e);
//        }// for mysql
//        dataSource.setPoolPreparedStatements(false);
    }

    //要考虑多线程的情况
    public Connection getConnection() throws Exception {
        Connection connection;
        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            throw new Exception(e);
        }
        return connection;
    }


//    public static Connection getConnection() throws SQLException {
//        String jdbcUrl = "jdbc:oracle:thin:@localhost:1521/xe";
//        String userid = "system";
//        String password = "123456";
//        OracleDataSource ds = new OracleDataSource();
//        ds.setURL(jdbcUrl);
//        return ds.getConnection(userid, password);
//    }
}