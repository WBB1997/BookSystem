package test;

import util.DruidManager;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;

public class databaseTest {
    public static void main(String[] args) {
        Connection connection;
        try {
            connection = DruidManager.getInstance().getConnection();
            CallableStatement callstate = connection.prepareCall("{? = call query_all(?)}");
            callstate.setString(2, "库");
            callstate.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
            callstate.execute();
            ResultSet r = (ResultSet) callstate.getObject(1);
            while (r.next()){
                System.out.println(r.getString("ISBN") + "     " + r.getString("Name"));
            }
        } catch (Exception s) {
            System.out.println(s.getMessage());
        }
    }
}
