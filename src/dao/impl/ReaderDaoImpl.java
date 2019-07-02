package dao.impl;

import dao.IReaderDao;
import javafx.util.Pair;
import model.*;
import oracle.jdbc.OracleTypes;
import util.DatabaseBean;
import util.DruidManager;
import util.MyUitl;
import util.SqlStateListener;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ReaderDaoImpl implements IReaderDao {
    @Override
    public void registerAccount(Reader r, SqlStateListener l) {
        Connection connection = null;
        CallableStatement callableStatement = null;
        try {
            connection = DatabaseBean.getConnection();
            callableStatement = connection.prepareCall("{call register_Account_Of_Reader(?, ?, ?, ?, ?, ?)}");
            fillPara_6(callableStatement, r);
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
    public void modAccount(Reader r, SqlStateListener l) {
        Connection connection = null;
        CallableStatement callableStatement = null;
        try {
            connection = DatabaseBean.getConnection();
            callableStatement = connection.prepareCall("{call mod_Account_Of_Reader(?, ?, ?, ?, ?, ?)}");
            fillPara_6(callableStatement, r);
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
    public void delAccount(Reader r, SqlStateListener l) {
        Connection connection = null;
        CallableStatement callableStatement = null;
        try {
            connection = DatabaseBean.getConnection();
            callableStatement = connection.prepareCall("{call del_Account_Of_Reader(?, ?)}");
            callableStatement.setString(1, r.getNo());
            callableStatement.setString(2, r.getPassword());
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
    public boolean checkReaderAccount(Reader r) throws Exception {
        int flag;
        Connection connection = DatabaseBean.getConnection();
        CallableStatement callableStatement = connection.prepareCall("{ ?=call Check_Reader_Account(?)}");
        callableStatement.registerOutParameter(1, OracleTypes.NUMBER);
        callableStatement.setString(2, r.getNo());
        callableStatement.execute();
        flag = callableStatement.getInt(1);
        DatabaseBean.close(null, callableStatement, connection);
        return flag != 0;
    }

    @Override
    public boolean checkReader(Reader r) throws Exception {
        int flag;
        Connection connection = DatabaseBean.getConnection();
        CallableStatement callableStatement = connection.prepareCall("{ ?=call Check_Reader(?, ?)}");
        callableStatement.registerOutParameter(1, OracleTypes.NUMBER);
        callableStatement.setString(2, r.getNo());
        callableStatement.setString(3, r.getPassword());
        callableStatement.execute();
        flag = callableStatement.getInt(1);
        DatabaseBean.close(null, callableStatement, connection);
        return flag != 0;
    }

    @Override
    public void borrowBook(Reader r, Book b, SqlStateListener l) {
        Connection connection = null;
        CallableStatement callableStatement = null;
        try {
            connection = DatabaseBean.getConnection();
            callableStatement = connection.prepareCall("{call Borrow_Book(?, ?, ?)}");
            fillPara_3(callableStatement, r, b);
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
    public void returnBook(Reader r, Book b, SqlStateListener l) {
        Connection connection = null;
        CallableStatement callableStatement = null;
        try {
            connection = DatabaseBean.getConnection();
            callableStatement = connection.prepareCall("{call Return_Book(?, ?, ?)}");
            fillPara_3(callableStatement, r, b);
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
    public Reader getReaderDetails(Reader r) throws SQLException {
        Connection connection;
        CallableStatement callableStatement;
        Reader readerDetails;
        connection = DatabaseBean.getConnection();
        callableStatement = connection.prepareCall("{? = call get_Reader_Details(?, ?)}");
        callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
        callableStatement.setString(2, r.getNo());
        callableStatement.setString(3, r.getPassword());
        callableStatement.execute();
        ResultSet resultSet = (ResultSet) callableStatement.getObject(1);
        readerDetails = fillReader(resultSet);
        DatabaseBean.close(null, callableStatement, connection);
        return readerDetails;
    }

    @Override
    public Pair<List<ReaderHistory>, Integer> queryHistory(Reader r, int pageNow, int pageSize) throws Exception {
        int count;
        Connection connection = DatabaseBean.getConnection();
        CallableStatement callableStatement = connection.prepareCall("{ ?=call query_reader_history(?, ?, ?, ?, ?)}");
        callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
        callableStatement.registerOutParameter(6, OracleTypes.NUMBER);
        callableStatement.setString(2, r.getNo());
        callableStatement.setString(3, r.getPassword());
        callableStatement.setInt(4, pageNow);
        callableStatement.setInt(5, pageSize);
        callableStatement.execute();
        ResultSet resultSet = (ResultSet) callableStatement.getObject(1);
        ArrayList<ReaderHistory> arrayList = getHistory(resultSet);
        count =  callableStatement.getInt(6);
        DatabaseBean.close(resultSet, callableStatement, connection);
        return new Pair<>(arrayList, count);
    }

    @Override
    public Pair<List<ReaderHistory>, Integer> queryHistoryInWord(Reader r, String ISBN, int pageNow, int pageSize) throws Exception {
        int count;
        Connection connection = DatabaseBean.getConnection();
        CallableStatement callableStatement = connection.prepareCall("{ ?=call query_reader_history_in_isbn(?, ?, ?, ?, ?, ?)}");
        callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
        callableStatement.registerOutParameter(7, OracleTypes.NUMBER);
        callableStatement.setString(2, r.getNo());
        callableStatement.setString(3, r.getPassword());
        callableStatement.setString(4, ISBN);
        callableStatement.setInt(5, pageNow);
        callableStatement.setInt(6, pageSize);
        callableStatement.execute();
        ResultSet resultSet = (ResultSet) callableStatement.getObject(1);
        ArrayList<ReaderHistory> arrayList = getHistory(resultSet);
        count =  callableStatement.getInt(7);
        DatabaseBean.close(resultSet, callableStatement, connection);
        return new Pair<>(arrayList, count);
    }

    @Override
    public List<String> getReaderTypes() throws SQLException {
        Connection connection = DatabaseBean.getConnection();
        CallableStatement callableStatement = connection.prepareCall("{ call query_reader_type(?)}");
        callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
        callableStatement.execute();
        ResultSet resultSet = (ResultSet) callableStatement.getObject(1);
        ArrayList<String> arrayList = new ArrayList<>();
        while (resultSet.next()){
            arrayList.add(resultSet.getString(1));
        }
        DatabaseBean.close(resultSet, callableStatement, connection);
        return arrayList;
    }

    @Override
    public List<String> getCollegeTypes() throws SQLException {
        Connection connection = DatabaseBean.getConnection();
        CallableStatement callableStatement = connection.prepareCall("{ call query_college_type(?)}");
        callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
        callableStatement.execute();
        ResultSet resultSet = (ResultSet) callableStatement.getObject(1);
        ArrayList<String> arrayList = new ArrayList<>();
        while (resultSet.next()){
            arrayList.add(resultSet.getString(1));
        }
        DatabaseBean.close(resultSet, callableStatement, connection);
        return arrayList;
    }


    private ArrayList<ReaderHistory> getHistory(ResultSet resultSet) throws SQLException {
        ArrayList<ReaderHistory> arrayList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        while (resultSet.next()) {
            ReaderHistory readerHistory = new ReaderHistory();
            readerHistory.setNo(resultSet.getString("No"));
            readerHistory.setISBN(resultSet.getString("ISBN"));
            readerHistory.setBorrowDate(sdf.format(resultSet.getDate("BorrowDate")));
            readerHistory.setShouldReturnDate(sdf.format(resultSet.getDate("ShouldReturnDate")));
            readerHistory.setCover(resultSet.getString("Cover"));
            readerHistory.setName(resultSet.getString("Name"));
            Date date = resultSet.getDate("ReturnDate");
            if (date == null)
                readerHistory.setReturnDate("-");
            else
                readerHistory.setReturnDate(sdf.format(resultSet.getDate(5)));
            arrayList.add(readerHistory);
        }
        return arrayList;
    }

    private void fillPara_6(CallableStatement callableStatement, Reader a) throws SQLException {
        callableStatement.setString(1, a.getNo());
        callableStatement.setString(2, a.getPassword());
        callableStatement.setString(3, a.getName());
        callableStatement.setString(4, a.getGender());
        callableStatement.setString(5, a.getType());
        callableStatement.setString(6, a.getCollege());
    }

    private void fillPara_3(CallableStatement callableStatement, Reader r, Book b) throws SQLException {
        callableStatement.setString(1, b.getISBN());
        callableStatement.setString(2, r.getNo());
        callableStatement.setString(3, r.getPassword());
    }

    private Reader fillReader(ResultSet resultSet) throws SQLException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Reader reader = new Reader();
        resultSet.next();
        reader.setNo(resultSet.getString(1));
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
        return reader;
    }
}
