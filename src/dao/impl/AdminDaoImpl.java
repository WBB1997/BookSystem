package dao.impl;

import com.alibaba.druid.proxy.jdbc.JdbcParameter;
import dao.IAdminDao;
import model.Admin;
import oracle.jdbc.OracleType;
import oracle.jdbc.OracleTypes;
import util.DruidManager;
import util.MyUitl;
import util.SqlStateListener;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class AdminDaoImpl implements IAdminDao {
    @Override
    public void registerAccount(Admin a, SqlStateListener l) {
        Connection connection;
        try {
            connection = DruidManager.getInstance().getConnection();
            CallableStatement callableStatement = connection.prepareCall("{call register_Account_Of_Admin(?, ?, ?, ?, ?)}");
            fillPara(callableStatement, a);
            callableStatement.execute();
            l.Correct();
        } catch (SQLException e){
            l.Error(e.getErrorCode(), MyUitl.DealWithErrMesage(e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void modAccount(Admin a, SqlStateListener l) {
        Connection connection;
        try {
            connection = DruidManager.getInstance().getConnection();
            CallableStatement callableStatement = connection.prepareCall("{call mod_Account_Of_Admin(?, ?, ?, ?, ?)}");
            fillPara(callableStatement, a);
            callableStatement.execute();
            l.Correct();
        } catch (SQLException e){
            l.Error(e.getErrorCode(), MyUitl.DealWithErrMesage(e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delAccount(Admin a, SqlStateListener l) {
        Connection connection;
        try {
            connection = DruidManager.getInstance().getConnection();
            CallableStatement callableStatement = connection.prepareCall("{call del_Account_Of_Admin(?, ?)}");
            callableStatement.setString(1, a.getNo());
            callableStatement.setString(2, a.getPassword());
            callableStatement.execute();
            l.Correct();
        } catch (SQLException e){
            l.Error(e.getErrorCode(), MyUitl.DealWithErrMesage(e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean checkAdminAccount(Admin a) throws Exception {
        int flag;
        Connection connection = DruidManager.getInstance().getConnection();
        CallableStatement callableStatement = connection.prepareCall("{ ?=call Check_Admin_Account(?)}");
        callableStatement.registerOutParameter(1, OracleTypes.NUMBER);
        callableStatement.setString(2, a.getNo());
        callableStatement.execute();
        flag = callableStatement.getInt(1);
        return flag != 0;
    }

    @Override
    public boolean checkAdmin(Admin a) throws Exception {
        int flag;
        Connection connection = DruidManager.getInstance().getConnection();
        CallableStatement callableStatement = connection.prepareCall("{ ?=call Check_Admin(?, ?)}");
        callableStatement.registerOutParameter(1, OracleTypes.NUMBER);
        callableStatement.setString(2, a.getNo());
        callableStatement.setString(3, a.getPassword());
        callableStatement.execute();
        flag = callableStatement.getInt(1);
        return flag != 0;
    }

    private void fillPara(CallableStatement callableStatement, Admin a) throws SQLException {
        callableStatement.setString(1, a.getNo());
        callableStatement.setString(2, a.getPassword());
        callableStatement.setString(3, a.getName());
        callableStatement.setString(4, a.getGender());
        callableStatement.setString(5, a.getPhone());
    }
}
