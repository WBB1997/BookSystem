package util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import oracle.jdbc.pool.OracleDataSource;


public class DatabaseBean {

    public static Connection getConnection() throws SQLException {
        String jdbcUrl = "JDBC:oracle:thin:@localhost:1521/orclpdb";
        String userid = "wubeibei";
        String password = "a690252189";
        OracleDataSource ds = new OracleDataSource();
        ds.setURL(jdbcUrl);
        return ds.getConnection(userid, password);
    }

    public static void close(ResultSet rs, Statement st, Connection conn) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}