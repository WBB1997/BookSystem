package dao.impl;

import dao.IReaderDao;
import model.Book;
import model.Reader;
import oracle.jdbc.OracleTypes;
import util.DatabaseBean;
import util.DruidManager;
import util.MyUitl;
import util.SqlStateListener;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

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
        } catch (SQLException e){
            l.Error(e.getErrorCode(), MyUitl.DealWithErrMesage(e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            DatabaseBean.close(null,callableStatement,connection);
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
        } catch (SQLException e){
            l.Error(e.getErrorCode(), MyUitl.DealWithErrMesage(e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            DatabaseBean.close(null,callableStatement,connection);
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
        } catch (SQLException e){
            l.Error(e.getErrorCode(), MyUitl.DealWithErrMesage(e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            DatabaseBean.close(null,callableStatement,connection);
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
        DatabaseBean.close(null,callableStatement,connection);
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
        DatabaseBean.close(null,callableStatement,connection);
        return flag != 0;
    }

    @Override
    public void borrowBook(Reader r, Book b, SqlStateListener l) {
        Connection connection = null;
        CallableStatement callableStatement = null;
        try {
            connection = DatabaseBean.getConnection();
            callableStatement = connection.prepareCall("{call Borrow_Book(?, ?, ?)}");
            fillPara_3(callableStatement,r,b);
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
    public void returnBook(Reader r, Book b, SqlStateListener l) {
        Connection connection = null;
        CallableStatement callableStatement = null;
        try {
            connection = DatabaseBean.getConnection();
            callableStatement = connection.prepareCall("{call Return_Book(?, ?, ?)}");
            fillPara_3(callableStatement,r,b);
            callableStatement.execute();
            l.Correct();
        } catch (SQLException e){
            l.Error(e.getErrorCode(), MyUitl.DealWithErrMesage(e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            DatabaseBean.close(null, callableStatement,connection);
        }
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
}
