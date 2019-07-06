package dao.impl;

import dao.IBookDao;
import javafx.util.Pair;
import model.Admin;
import model.Book;
import model.Reader;
import model.Staff;
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
    public void addBook(Staff staff, Book b, SqlStateListener l) {
        Connection connection = null;
        CallableStatement callableStatement = null;
        try {
            connection = DatabaseBean.getConnection();
            callableStatement = connection.prepareCall("{call add_Book(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
            callableStatement.setString("Staff_Account", staff.getNo());
            callableStatement.setString("Staff_PassWord", staff.getPassword());
            callableStatement.setString("Book_ISBN", b.getISBN());
            callableStatement.setString("Book_Name", b.getName());
            callableStatement.setString("Book_Author", b.getAuthor());
            callableStatement.setString("Book_Type", b.getType());
            callableStatement.setString("Book_Publisher", b.getPublisher());
            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM");
            java.util.Date date1 = sDateFormat.parse(b.getPublishDate());
            Date date = new Date(date1.getTime());
            callableStatement.setDate("Book_PublishDate", date);
            callableStatement.setDouble("Book_Value", b.getValue());
            callableStatement.setInt("Book_Amount", b.getAmount());
            callableStatement.setInt("Book_Available", b.getAvailable());
            callableStatement.setString("Book_Cover", b.getCover());
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
    public void increaseBook(Book b, int quantity, Staff staff, SqlStateListener l) {
        Connection connection = null;
        CallableStatement callableStatement = null;
        try {
            connection = DatabaseBean.getConnection();
            callableStatement = connection.prepareCall("{call increase_Book(?, ?, ?, ?)}");
            callableStatement.setString("Book_ISBN", b.getISBN());
            callableStatement.setInt("Quantity", quantity);
            callableStatement.setString("Staff_Account", staff.getNo());
            callableStatement.setString("Staff_PassWord", staff.getPassword());
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
    public void decreaseBook(Book b, int quantity, Staff staff, SqlStateListener l) {
        Connection connection = null;
        CallableStatement callableStatement = null;
        try {
            connection = DatabaseBean.getConnection();
            callableStatement = connection.prepareCall("{call decrease_Book(?, ?, ?, ?)}");
            callableStatement.setString("Book_ISBN", b.getISBN());
            callableStatement.setInt("Quantity", quantity);
            callableStatement.setString("Staff_Account", staff.getNo());
            callableStatement.setString("Staff_PassWord", staff.getPassword());
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
    public void modBook(Book b, Staff staff, SqlStateListener l) {
        Connection connection = null;
        CallableStatement callableStatement = null;
        try {
            connection = DatabaseBean.getConnection();
            callableStatement = connection.prepareCall("{call mod_Book(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
            callableStatement.setString("Staff_Account", staff.getNo());
            callableStatement.setString("Staff_PassWord", staff.getPassword());
            callableStatement.setString("Book_ISBN", b.getISBN());
            callableStatement.setString("Book_Name", b.getName());
            callableStatement.setString("Book_Author", b.getAuthor());
            callableStatement.setString("Book_Type", b.getType());
            callableStatement.setString("Book_Publisher", b.getPublisher());
            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM");
            java.util.Date date1 = sDateFormat.parse(b.getPublishDate());
            Date date = new Date(date1.getTime());
            callableStatement.setDate("Book_PublishDate", date);
            callableStatement.setDouble("Book_Value", b.getValue());
            callableStatement.setString("Book_Cover", b.getCover());
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
            arrayList.add(resultSet.getString(2));
        }
        DatabaseBean.close(resultSet, callableStatement, connection);
        return arrayList;
    }

    private ArrayList<Book> getBook(ResultSet resultSet) throws SQLException {
        ArrayList<Book> bookArrayList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        while (resultSet.next()) {
            Book b = new Book();
            b.setISBN(resultSet.getString("ISBN"));
            b.setName(resultSet.getString("Name"));
            b.setAuthor(resultSet.getString("Author"));
            b.setType(resultSet.getString("Type"));
            b.setPublisher(resultSet.getString("Publisher"));
            b.setPublishDate(sdf.format(resultSet.getDate("PublishDate")));
            b.setAmount(resultSet.getInt("Amount"));
            b.setAvailable(resultSet.getInt("Available"));
            b.setCover(resultSet.getString("Cover"));
            b.setValue(resultSet.getDouble("Value"));
            bookArrayList.add(b);
        }
        return bookArrayList;
    }

    public void PrivateAaddBook(Staff s, Book b, SqlStateListener l) {
        Connection connection = null;
        CallableStatement callableStatement = null;
        try {
            connection = DatabaseBean.getConnection();
            callableStatement = connection.prepareCall("{call add_Book(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ,?)}");
            callableStatement.setString("Staff_Account", s.getNo());
            callableStatement.setString("Staff_PassWord", s.getPassword());
            callableStatement.setString("Book_ISBN", b.getISBN());
            callableStatement.setString("Book_Name", b.getName());
            callableStatement.setString("Book_Author", b.getAuthor());
            callableStatement.setString("Book_Type", b.getType());
            callableStatement.setString("Book_Publisher", b.getPublisher());
            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM");
            java.util.Date date1 = sDateFormat.parse(b.getPublishDate());
            Date date = new Date(date1.getTime());
            callableStatement.setDate("Book_PublishDate", date);
            callableStatement.setDouble("Book_Value", b.getValue());
            callableStatement.setInt("Book_Amount", b.getAmount());
            callableStatement.setInt("Book_Available", b.getAvailable());
            callableStatement.setString("Book_Cover", b.getCover());
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
