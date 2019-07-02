package dao.impl;

import dao.IAdminDao;
import javafx.util.Pair;
import model.Admin;
import model.AdminHistory;
import model.Reader;
import oracle.jdbc.OracleTypes;
import util.DatabaseBean;
import util.MyUitl;
import util.SqlStateListener;

import java.sql.*;
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
        } catch (SQLException e) {
            l.Error(e.getErrorCode(), MyUitl.DealWithErrMesage(e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseBean.close(null, callableStatement, connection);
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
        } catch (SQLException e) {
            l.Error(e.getErrorCode(), MyUitl.DealWithErrMesage(e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseBean.close(null, callableStatement, connection);
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
        } catch (SQLException e) {
            l.Error(e.getErrorCode(), MyUitl.DealWithErrMesage(e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseBean.close(null, callableStatement, connection);
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
    public Pair<List<AdminHistory>, Integer> queryHistory(Admin a, int pageNow, int pageSize) throws Exception {
        int count;
        Connection connection = DatabaseBean.getConnection();
        CallableStatement callableStatement = connection.prepareCall("{ ?=call query_admin_history(?, ?, ?, ?, ?)}");
        fillPara_6(callableStatement,a,pageNow,pageSize);
        callableStatement.execute();
        ResultSet resultSet = (ResultSet) callableStatement.getObject(1);
        ArrayList<AdminHistory> arrayList = getHistory(resultSet);
        count =  callableStatement.getInt(6);
        DatabaseBean.close(resultSet, callableStatement, connection);
        return new Pair<>(arrayList, count);
    }

    @Override
    public Pair<List<AdminHistory>, Integer> queryHistoryInWord(Admin a, String ISBN, int pageNow, int pageSize) throws Exception {
        int count;
        Connection connection = DatabaseBean.getConnection();
        CallableStatement callableStatement = connection.prepareCall("{ ?=call query_admin_history_in_isbn(?, ?, ?, ?, ?, ?)}");
        callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
        callableStatement.registerOutParameter(7, OracleTypes.NUMBER);
        callableStatement.setString(2, a.getNo());
        callableStatement.setString(3, a.getPassword());
        callableStatement.setString(4, ISBN);
        callableStatement.setInt(5, pageNow);
        callableStatement.setInt(6, pageSize);
        callableStatement.execute();
        ResultSet resultSet = (ResultSet) callableStatement.getObject(1);
        ArrayList<AdminHistory> arrayList = getHistory(resultSet);
        count =  callableStatement.getInt(7);
        DatabaseBean.close(resultSet, callableStatement, connection);
        return new Pair<>(arrayList, count);
    }

    @Override
    public Pair<List<Reader>, Integer> getReaderList(Admin a, int pageNow, int pageSize) throws SQLException {
        int count;
        Connection connection = DatabaseBean.getConnection();
        CallableStatement callableStatement = connection.prepareCall("{ ?=call query_reader_by_admin(?, ?, ?, ?, ?)}");
        fillPara_6(callableStatement,a,pageNow,pageSize);
        callableStatement.execute();
        ResultSet resultSet = (ResultSet) callableStatement.getObject(1);
        count =  callableStatement.getInt(6);
        ArrayList<Reader> arrayList = getReader(resultSet);
        DatabaseBean.close(resultSet, callableStatement, connection);
        return new Pair<>(arrayList, count);
    }

    @Override
    public Admin getAdminDetails(Admin a) throws SQLException {
        Connection connection;
        CallableStatement callableStatement;
        connection = DatabaseBean.getConnection();
        callableStatement = connection.prepareCall("{ ? = call get_Admin_Details(?, ?)}");
        callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
        callableStatement.setString(2, a.getNo());
        callableStatement.setString(3, a.getPassword());
        callableStatement.execute();
        ResultSet resultSet = (ResultSet) callableStatement.getObject(1);
        Admin adminDetails = fillAdmin(resultSet);
        DatabaseBean.close(null, callableStatement, connection);
        return adminDetails;
    }

    private ArrayList<AdminHistory> getHistory(ResultSet resultSet) throws SQLException {
        ArrayList<AdminHistory> arrayList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        while (resultSet.next()) {
            AdminHistory adminHistory = new AdminHistory();
            adminHistory.setNo(resultSet.getString("NO"));
            adminHistory.setISBN(resultSet.getString("ISBN"));
            adminHistory.setPurchaseDate(sdf.format(resultSet.getDate("PurchaseDate")));
            adminHistory.setPurchaseAmount(resultSet.getInt("PurchaseAmount"));
            adminHistory.setName(resultSet.getString("Name"));
            adminHistory.setCover(resultSet.getString("Cover"));
            arrayList.add(adminHistory);
        }
        return arrayList;
    }

    private ArrayList<Reader> getReader(ResultSet resultSet) throws SQLException {
        ArrayList<Reader> arrayList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        while (resultSet.next()) {
            Reader reader = new Reader();
            reader.setNo(resultSet.getString(1));
            reader.setPassword("***********");
            reader.setName(resultSet.getString(2));
            reader.setGender(resultSet.getString(3));
            reader.setType(resultSet.getString(4));
            reader.setCollege(resultSet.getString(5));
            reader.setTakeEffectDate(sdf.format(resultSet.getDate(6)));
            Date date = resultSet.getDate(7);
            if (date == null)
                reader.setLoseEffectDate("-");
            else
                reader.setLoseEffectDate(sdf.format(resultSet.getDate(7)));
            arrayList.add(reader);
        }
        return arrayList;
    }

    private Admin fillAdmin(ResultSet resultSet) throws SQLException {
        Admin admin = new Admin();
        resultSet.next();
        admin.setNo(resultSet.getString(1));
        admin.setPassword(resultSet.getString(2));
        admin.setName(resultSet.getString(3));
        admin.setGender(resultSet.getString(4));
        admin.setPhone(resultSet.getString(5));
        return admin;
    }


    private void fillPara(CallableStatement callableStatement, Admin a) throws SQLException {
        callableStatement.setString(1, a.getNo());
        callableStatement.setString(2, a.getPassword());
        callableStatement.setString(3, a.getName());
        callableStatement.setString(4, a.getGender());
        callableStatement.setString(5, a.getPhone());
    }

    private void fillPara_6(CallableStatement callableStatement, Admin a, int pageNow, int pageSize) throws SQLException {
        callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
        callableStatement.registerOutParameter(6, OracleTypes.NUMBER);
        callableStatement.setString(2, a.getNo());
        callableStatement.setString(3, a.getPassword());
        callableStatement.setInt(4, pageNow);
        callableStatement.setInt(5, pageSize);
    }

}
