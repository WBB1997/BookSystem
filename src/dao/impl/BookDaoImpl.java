package dao.impl;

import dao.IBookDao;
import javafx.util.Pair;
import model.Admin;
import model.Book;
import model.Reader;
import oracle.jdbc.OracleTypes;
import util.DatabaseBean;
import util.MyUitl;
import util.SqlStateListener;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class BookDaoImpl implements IBookDao {
    @Override
    public void addBook(Admin a, Book b, SqlStateListener l) {
        Connection connection = null;
        CallableStatement callableStatement = null;
        try {
            connection = DatabaseBean.getConnection();
            callableStatement = connection.prepareCall("{call add_Book(?, ?, ?, ?, ?, ?, ?, ?)}");
            fillPara_11(callableStatement, a, b);
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
    public void increaseBook(Book b, int quantity, Admin a, SqlStateListener l) {
        Connection connection = null;
        CallableStatement callableStatement = null;
        try {
            connection = DatabaseBean.getConnection();
            callableStatement = connection.prepareCall("{call increase_Book(?, ?, ?, ?)}");
            fillPara_4(callableStatement, b, quantity, a);
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
    public void decreaseBook(Book b, int quantity, Admin a, SqlStateListener l) {
        Connection connection = null;
        CallableStatement callableStatement = null;
        try {
            connection = DatabaseBean.getConnection();
            callableStatement = connection.prepareCall("{call decrease_Book(?, ?, ?, ?)}");
            fillPara_4(callableStatement, b, quantity, a);
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
    public void modBook(Book b, Admin a, SqlStateListener l) {
        Connection connection = null;
        CallableStatement callableStatement = null;
        try {
            connection = DatabaseBean.getConnection();
            callableStatement = connection.prepareCall("{call mod_Book(?, ?, ?, ?, ?, ?, ?, ?)}");
            fillPara_11(callableStatement, a, b);
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
    public boolean checkBookOfISBN(Book b) throws Exception {
        int flag;
        Connection connection = DatabaseBean.getConnection();
        CallableStatement callableStatement = connection.prepareCall("{ ?=call Check_Book_ISBN(?)}");
        callableStatement.registerOutParameter(1, OracleTypes.NUMBER);
        callableStatement.setString(2, b.getISBN());
        callableStatement.execute();
        flag = callableStatement.getInt(1);
        DatabaseBean.close(null, callableStatement, connection);
        return flag != 0;
    }

    @Override
    public boolean isReaderBorrowBook(Book b, Reader r) throws Exception {
        int flag;
        Connection connection = DatabaseBean.getConnection();
        CallableStatement callableStatement = connection.prepareCall("{ ?=call Check_Reader_Borrow_Book(?, ?)}");
        callableStatement.registerOutParameter(1, OracleTypes.NUMBER);
        callableStatement.setString(2, b.getISBN());
        callableStatement.setString(3, r.getNo());
        callableStatement.execute();
        flag = callableStatement.getInt(1);
        DatabaseBean.close(null, callableStatement, connection);
        return flag != 0;
    }

    @Override
    public Pair<List<Book>, Integer> queryAllBook(String word, int pageNow, int pageSize) throws Exception {
        int count;
        Connection connection = DatabaseBean.getConnection();
        CallableStatement callableStatement = connection.prepareCall("{ ?=call query_all(?, ?, ?, ?)}");
        callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
        callableStatement.registerOutParameter(5, OracleTypes.NUMBER);
        callableStatement.setString(2, word);
        callableStatement.setInt(3, pageNow);
        callableStatement.setInt(4, pageSize);
        callableStatement.execute();
        ResultSet resultSet = (ResultSet) callableStatement.getObject(1);
        count = callableStatement.getInt(5);
        ArrayList<Book> arrayList = getBook(resultSet);
        DatabaseBean.close(resultSet, callableStatement, connection);
        return new Pair<>(arrayList, count);
    }

    @Override
    public Pair<List<Book>, Integer> queryBookOfKeyword(String parameter, String word, int pageNow, int pageSize) throws Exception {
        int count = 0;
        Connection connection = DatabaseBean.getConnection();
        CallableStatement callableStatement = connection.prepareCall("{ ?=call query_key_word(?, ?, ?, ?, ?)}");
        callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
        callableStatement.registerOutParameter(6, OracleTypes.NUMBER);
        callableStatement.setString(2, parameter);
        callableStatement.setString(3, word);
        callableStatement.setInt(4, pageNow);
        callableStatement.setInt(5, pageSize);
        callableStatement.execute();
        ResultSet resultSet = (ResultSet) callableStatement.getObject(1);
        count = callableStatement.getInt(6);
        ArrayList<Book> arrayList = getBook(resultSet);
        DatabaseBean.close(resultSet, callableStatement, connection);
        return new Pair<>(arrayList, count);
    }

    @Override
    public List<String> getBookTypes() throws SQLException {
        Connection connection = DatabaseBean.getConnection();
        CallableStatement callableStatement = connection.prepareCall("{ call query_book_type(?)}");
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

    private void fillPara_11(CallableStatement callableStatement, Admin a, Book b) throws SQLException, ParseException {
        callableStatement.setString(1, a.getNo());
        callableStatement.setString(2, a.getPassword());
        callableStatement.setString(3, b.getISBN());
        callableStatement.setString(4, b.getName());
        callableStatement.setString(5, b.getAuthor());
        callableStatement.setString(6, b.getType());
        callableStatement.setString(7, b.getPublisher());
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM");
        java.util.Date date1 = sDateFormat.parse(b.getPublishDate());
        Date date = new Date(date1.getTime());
        callableStatement.setDate(8, date);
    }

    private void fillPara_4(CallableStatement callableStatement, Book b, int quantity, Admin a) throws SQLException {
        callableStatement.setString(1, b.getISBN());
        callableStatement.setInt(2, quantity);
        callableStatement.setString(3, a.getNo());
        callableStatement.setString(4, a.getPassword());
    }

    private ArrayList<Book> getBook(ResultSet resultSet) throws SQLException {
        ArrayList<Book> bookArrayList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        while (resultSet.next()) {
            Book b = new Book();
            b.setISBN(resultSet.getString(1));
            b.setName(resultSet.getString(2));
            b.setAuthor(resultSet.getString(3));
            b.setType(resultSet.getString(4));
            b.setPublisher(resultSet.getString(5));
            b.setPublishDate(sdf.format(resultSet.getDate(6)));
            b.setAmount(resultSet.getInt(7));
            b.setAvailable(resultSet.getInt(8));
            b.setCover(resultSet.getString(9));
            bookArrayList.add(b);
        }
        return bookArrayList;
    }

    public void PrivateAaddBook(Admin a, Book b, SqlStateListener l) {
        Connection connection = null;
        CallableStatement callableStatement = null;
        try {
            connection = DatabaseBean.getConnection();
            callableStatement = connection.prepareCall("{call add_Book(?, ?, ?, ?, ?, ?, ?, ?, ?, ? ,?)}");
            callableStatement.setString(1, a.getNo());
            callableStatement.setString(2, a.getPassword());
            callableStatement.setString(3, b.getISBN());
            callableStatement.setString(4, b.getName());
            callableStatement.setString(5, b.getAuthor());
            callableStatement.setString(6, b.getType());
            callableStatement.setString(7, b.getPublisher());
            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM");
            java.util.Date date1 = sDateFormat.parse(b.getPublishDate());
            Date date = new Date(date1.getTime());
            callableStatement.setDate(8, date);
            callableStatement.setInt(9, b.getAmount());
            callableStatement.setInt(10, b.getAvailable());
            callableStatement.setString(11, b.getCover());
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
}
