package dao.impl;

import com.alibaba.druid.proxy.jdbc.JdbcParameter;
import dao.IAdminDao;
import model.Admin;
import model.AdminHistory;
import model.Book;
import oracle.jdbc.OracleType;
import oracle.jdbc.OracleTypes;
import util.DatabaseBean;
import util.DruidManager;
import util.MyUitl;
import util.SqlStateListener;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class AdminDaoImpl implements IAdminDao {
    @Override
    public void registerAccount(Admin a, SqlStateListener l) {
        Connection connection = null;
        CallableStatement callableStatement = null;
        try {
            connection = DatabaseBean.getConnection();
            callableStatement = connection.prepareCall("{call register_Account_Of_Admin(?, ?, ?, ?, ?)}");
            fillPara(callableStatement, a);
            callableStatement.execute();
            l.Correct();
        } catch (SQLException e){
            l.Error(e.getErrorCode(), MyUitl.DealWithErrMesage(e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            DatabaseBean.close(null,callableStatement,connection);
        }
    }

    @Override
    public void modAccount(Admin a, SqlStateListener l) {
        Connection connection = null;
        CallableStatement callableStatement = null;
        try {
            connection = DatabaseBean.getConnection();
            callableStatement = connection.prepareCall("{call mod_Account_Of_Admin(?, ?, ?, ?, ?)}");
            fillPara(callableStatement, a);
            callableStatement.execute();
            l.Correct();
        } catch (SQLException e){
            l.Error(e.getErrorCode(), MyUitl.DealWithErrMesage(e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            DatabaseBean.close(null,callableStatement,connection);
        }
    }

    @Override
    public void delAccount(Admin a, SqlStateListener l) {
        Connection connection = null;
        CallableStatement callableStatement = null;
        try {
            connection = DatabaseBean.getConnection();
            callableStatement = connection.prepareCall("{call del_Account_Of_Admin(?, ?)}");
            callableStatement.setString(1, a.getNo());
            callableStatement.setString(2, a.getPassword());
            callableStatement.execute();
            l.Correct();
        } catch (SQLException e){
            l.Error(e.getErrorCode(), MyUitl.DealWithErrMesage(e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            DatabaseBean.close(null,callableStatement,connection);
        }
    }

    @Override
    public boolean checkAdminAccount(Admin a) throws Exception {
        int flag;
        Connection connection = DatabaseBean.getConnection();
        CallableStatement callableStatement = connection.prepareCall("{ ?=call Check_Admin_Account(?)}");
        callableStatement.registerOutParameter(1, OracleTypes.NUMBER);
        callableStatement.setString(2, a.getNo());
        callableStatement.execute();
        flag = callableStatement.getInt(1);
        DatabaseBean.close(null, callableStatement, connection);
        return flag != 0;
    }

    @Override
    public boolean checkAdmin(Admin a) throws Exception {
        int flag;
        Connection connection = DatabaseBean.getConnection();
        CallableStatement callableStatement = connection.prepareCall("{ ?=call Check_Admin(?, ?)}");
        callableStatement.registerOutParameter(1, OracleTypes.NUMBER);
        callableStatement.setString(2, a.getNo());
        callableStatement.setString(3, a.getPassword());
        callableStatement.execute();
        flag = callableStatement.getInt(1);
        DatabaseBean.close(null, callableStatement, connection);
        return flag != 0;
    }

    @Override
    public List<AdminHistory> queryHistory(Admin a, int pageNow, int pageSize) throws Exception {
        Connection connection = DatabaseBean.getConnection();
        CallableStatement callableStatement = connection.prepareCall("{ ?=call query_admin_history(?, ?, ?, ?)}");
        callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
        callableStatement.setString(2, a.getNo());
        callableStatement.setString(3, a.getPassword());
        callableStatement.setInt(4, pageNow);
        callableStatement.setInt(5, pageSize);
        callableStatement.execute();
        ResultSet resultSet = (ResultSet) callableStatement.getObject(1);
        ArrayList<AdminHistory> arrayList = getHistory(resultSet);
        DatabaseBean.close(resultSet, callableStatement, connection);
        return arrayList;
    }

    @Override
    public int countHistory() throws Exception {
        return MyUitl.count("Admin_Book");
    }

    private void fillPara(CallableStatement callableStatement, Admin a) throws SQLException {
        callableStatement.setString(1, a.getNo());
        callableStatement.setString(2, a.getPassword());
        callableStatement.setString(3, a.getName());
        callableStatement.setString(4, a.getGender());
        callableStatement.setString(5, a.getPhone());
    }

    private ArrayList<AdminHistory> getHistory(ResultSet resultSet) throws SQLException {
        ArrayList<AdminHistory> arrayList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        while (resultSet.next()){
            AdminHistory adminHistory = new AdminHistory();
            adminHistory.setNo(resultSet.getString(1));
            adminHistory.setISBN(resultSet.getString(2));
            adminHistory.setPurchaseDate(sdf.format(resultSet.getDate(3)));
            adminHistory.setPurchaseAmount(resultSet.getInt(4));
            arrayList.add(adminHistory);
        }
        return arrayList;
    }
}
