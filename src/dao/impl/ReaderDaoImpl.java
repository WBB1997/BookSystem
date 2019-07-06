package dao.impl;

import dao.IReaderDao;
import javafx.util.Pair;
import model.*;
import oracle.jdbc.OracleTypes;
import util.DatabaseBean;
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
            callableStatement = connection.prepareCall("{call del_Account_Of_Reader(?)}");
            callableStatement.setString(1, r.getNo());
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
            callableStatement = connection.prepareCall("{call Borrow_Book(?, ?)}");
            callableStatement.setString(1, b.getISBN());
            callableStatement.setString(2, r.getNo());
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
            callableStatement = connection.prepareCall("{call Return_Book(?, ?)}");
            callableStatement.setString("Book_ISBN", b.getISBN());
            callableStatement.setString("Reader_No", r.getNo());
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
    public Pair<List<Reader>, Integer> getReaderList(int pageNow, int pageSize) throws SQLException {
        int count;
        Connection connection = DatabaseBean.getConnection();
        CallableStatement callableStatement = connection.prepareCall("{ ?=call query_reader_list(?, ?, ?)}");
        callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
        callableStatement.registerOutParameter(4, OracleTypes.NUMBER);
        callableStatement.setInt(2, pageNow);
        callableStatement.setInt(3, pageSize);
        callableStatement.execute();
        ResultSet resultSet = (ResultSet) callableStatement.getObject(1);
        count =  callableStatement.getInt(4);
        ArrayList<Reader> arrayList = getReader(resultSet);
        DatabaseBean.close(resultSet, callableStatement, connection);
        return new Pair<>(arrayList, count);
    }

    @Override
    public Pair<List<Reader>, Integer> getReaderListInKeyWord(String keyword, int pageNow, int pageSize) throws SQLException {
        int count;
        Connection connection = DatabaseBean.getConnection();
        CallableStatement callableStatement = connection.prepareCall("{ ?=call query_reader_list_in_keyword(?, ?, ?, ?)}");
        callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
        callableStatement.registerOutParameter(5, OracleTypes.NUMBER);
        callableStatement.setString(2,keyword);
        callableStatement.setInt(3, pageNow);
        callableStatement.setInt(4, pageSize);
        callableStatement.execute();
        ResultSet resultSet = (ResultSet) callableStatement.getObject(1);
        count =  callableStatement.getInt(5);
        ArrayList<Reader> arrayList = getReader(resultSet);
        DatabaseBean.close(resultSet, callableStatement, connection);
        return new Pair<>(arrayList, count);
    }

    @Override
    public Pair<List<Reader_Borrow_Return_History>, Integer> queryHistory(Reader r, int pageNow, int pageSize) throws Exception {
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
        ArrayList<Reader_Borrow_Return_History> arrayList = getHistory(resultSet);
        count =  callableStatement.getInt(6);
        DatabaseBean.close(resultSet, callableStatement, connection);
        return new Pair<>(arrayList, count);
    }

    @Override
    public Pair<List<Reader_Borrow_Return_History>, Integer> queryHistoryInWord(Reader r, String ISBN, int pageNow, int pageSize) throws Exception {
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
        ArrayList<Reader_Borrow_Return_History> arrayList = getHistory(resultSet);
        count =  callableStatement.getInt(7);
        DatabaseBean.close(resultSet, callableStatement, connection);
        return new Pair<>(arrayList, count);
    }

    @Override
    public Pair<List<Staff_DealWith_Reader_Borrow_History>, Integer> queryStaffDealWithReaderBorrowHistory(Reader r, int pageNow, int pageSize) throws Exception {
        int count;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Connection connection = DatabaseBean.getConnection();
        CallableStatement callableStatement = connection.prepareCall("{ ?=call query_Staff_DealWith_Reader_Borrow_History(?, ?, ?, ?)}");
        callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
        callableStatement.registerOutParameter(5, OracleTypes.NUMBER);
        callableStatement.setString(2, r.getNo());
        callableStatement.setInt(3, pageNow);
        callableStatement.setInt(4, pageSize);
        callableStatement.execute();
        ResultSet resultSet = (ResultSet) callableStatement.getObject(1);
        ArrayList<Staff_DealWith_Reader_Borrow_History> arrayList = new ArrayList<>();
        while (resultSet.next()){
            Staff_DealWith_Reader_Borrow_History history = new Staff_DealWith_Reader_Borrow_History();
            Book book = new Book();
            Staff staff = new Staff();
            book.setName(resultSet.getString("Name"));
            book.setISBN(resultSet.getString("ISBN"));
            book.setCover(resultSet.getString("Cover"));
            staff.setNo(resultSet.getString("Staff_No"));
            history.setBook(book);
            history.setStaff(staff);
            history.setStatus(resultSet.getString("Status"));
            history.setReader(r);
            Date date = resultSet.getDate("Time");
            if (date == null)
                history.setTime("-");
            else
                history.setTime(sdf.format(resultSet.getDate("Time")));
            arrayList.add(history);
        }
        count =  callableStatement.getInt(5);
        DatabaseBean.close(resultSet, callableStatement, connection);
        return new Pair<>(arrayList, count);
    }

    @Override
    public Pair<List<Staff_DealWith_Reader_Borrow_History>, Integer> queryStaffDealWithReaderBorrowHistoryInWord(Reader r, String ISBN, int pageNow, int pageSize) throws Exception {
        int count;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Connection connection = DatabaseBean.getConnection();
        CallableStatement callableStatement = connection.prepareCall("{ ?=call query_Staff_DealWith_Reader_Borrow_History_With_Keyword(?, ?, ?, ?, ?)}");
        callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
        callableStatement.registerOutParameter(6, OracleTypes.NUMBER);
        callableStatement.setString(2, r.getNo());
        callableStatement.setString(3, ISBN);
        callableStatement.setInt(4, pageNow);
        callableStatement.setInt(5, pageSize);
        callableStatement.execute();
        ResultSet resultSet = (ResultSet) callableStatement.getObject(1);
        ArrayList<Staff_DealWith_Reader_Borrow_History> arrayList = new ArrayList<>();
        while (resultSet.next()){
            Staff_DealWith_Reader_Borrow_History history = new Staff_DealWith_Reader_Borrow_History();
            Book book = new Book();
            Staff staff = new Staff();
            book.setName(resultSet.getString("Name"));
            book.setISBN(resultSet.getString("ISBN"));
            book.setCover(resultSet.getString("Cover"));
            staff.setNo(resultSet.getString("Staff_No"));
            history.setBook(book);
            history.setStaff(staff);
            history.setStatus(resultSet.getString("Status"));
            history.setReader(r);
            Date date = resultSet.getDate("Time");
            if (date == null)
                history.setTime("-");
            else
                history.setTime(sdf.format(resultSet.getDate("Time")));
            arrayList.add(history);
        }
        count =  callableStatement.getInt(6);
        DatabaseBean.close(resultSet, callableStatement, connection);
        return new Pair<>(arrayList, count);
    }

    @Override
    public Pair<List<Staff_DealWith_Reader_Return_History>, Integer> queryStaffDealWithReaderReturnHistory(Reader r, int pageNow, int pageSize) throws Exception {
        int count;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Connection connection = DatabaseBean.getConnection();
        CallableStatement callableStatement = connection.prepareCall("{ ?=call query_Staff_DealWith_Reader_Return_History(?, ?, ?, ?)}");
        callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
        callableStatement.registerOutParameter(5, OracleTypes.NUMBER);
        callableStatement.setString(2, r.getNo());
        callableStatement.setInt(3, pageNow);
        callableStatement.setInt(4, pageSize);
        callableStatement.execute();
        ResultSet resultSet = (ResultSet) callableStatement.getObject(1);
        ArrayList<Staff_DealWith_Reader_Return_History> arrayList = new ArrayList<>();
        while (resultSet.next()){
            Staff_DealWith_Reader_Return_History history = new Staff_DealWith_Reader_Return_History();
            Book book = new Book();
            Staff staff = new Staff();
            book.setName(resultSet.getString("Name"));
            book.setISBN(resultSet.getString("ISBN"));
            book.setCover(resultSet.getString("Cover"));
            staff.setNo(resultSet.getString("Staff_No"));
            history.setBook(book);
            history.setStaff(staff);
            history.setStatus(resultSet.getString("Status"));
            history.setReader(r);
            Date date = resultSet.getDate("Time");
            if (date == null)
                history.setTime("-");
            else
                history.setTime(sdf.format(resultSet.getDate("Time")));
            arrayList.add(history);
        }
        count =  callableStatement.getInt(5);
        DatabaseBean.close(resultSet, callableStatement, connection);
        return new Pair<>(arrayList, count);
    }

    @Override
    public Pair<List<Staff_DealWith_Reader_Return_History>, Integer> queryStaffDealWithReaderReturnHistoryInWord(Reader r, String ISBN, int pageNow, int pageSize) throws Exception {
        int count;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Connection connection = DatabaseBean.getConnection();
        CallableStatement callableStatement = connection.prepareCall("{ ?=call query_Staff_DealWith_Reader_Return_History_With_Keyword(?, ?, ?, ?, ?)}");
        callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
        callableStatement.registerOutParameter(6, OracleTypes.NUMBER);
        callableStatement.setString(2, r.getNo());
        callableStatement.setString(3, ISBN);
        callableStatement.setInt(4, pageNow);
        callableStatement.setInt(5, pageSize);
        callableStatement.execute();
        ResultSet resultSet = (ResultSet) callableStatement.getObject(1);
        ArrayList<Staff_DealWith_Reader_Return_History> arrayList = new ArrayList<>();
        while (resultSet.next()){
            Staff_DealWith_Reader_Return_History history = new Staff_DealWith_Reader_Return_History();
            Book book = new Book();
            Staff staff = new Staff();
            book.setName(resultSet.getString("Name"));
            book.setISBN(resultSet.getString("ISBN"));
            book.setCover(resultSet.getString("Cover"));
            staff.setNo(resultSet.getString("Staff_No"));
            history.setBook(book);
            history.setStaff(staff);
            history.setStatus(resultSet.getString("Status"));
            history.setReader(r);
            Date date = resultSet.getDate("Time");
            if (date == null)
                history.setTime("-");
            else
                history.setTime(sdf.format(resultSet.getDate("Time")));
            arrayList.add(history);
        }
        count =  callableStatement.getInt(6);
        DatabaseBean.close(resultSet, callableStatement, connection);
        return new Pair<>(arrayList, count);
    }

    @Override
    public Pair<List<Reader_Message>, Integer> getMessageList(Reader r, int pageNow, int pageSize) throws SQLException {
        int count;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Connection connection = DatabaseBean.getConnection();
        CallableStatement callableStatement = connection.prepareCall("{ ?=call get_Reader_Message(?, ?, ?, ?)}");
        callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
        callableStatement.registerOutParameter(5, OracleTypes.NUMBER);
        callableStatement.setString(2, r.getNo());
        callableStatement.setInt(3, pageNow);
        callableStatement.setInt(4, pageSize);
        callableStatement.execute();
        ResultSet resultSet = (ResultSet) callableStatement.getObject(1);
        ArrayList<Reader_Message> arrayList = new ArrayList<>();
        while (resultSet.next()){
            Reader_Message reader_message = new Reader_Message();
            reader_message.setReader(r);
            reader_message.setMessage(resultSet.getString("Message"));
            reader_message.setStatus(resultSet.getString("Status"));
            reader_message.setTitle(resultSet.getString("Title"));
            Date date = resultSet.getDate("Time");
            if (date == null)
                reader_message.setTime("-");
            else
                reader_message.setTime(sdf.format(resultSet.getDate("Time")));
            arrayList.add(reader_message);
        }
        count =  callableStatement.getInt(5);
        DatabaseBean.close(resultSet, callableStatement, connection);
        return new Pair<>(arrayList, count);
    }

    @Override
    public void cancelBorrowApply(Reader r, Book b, SqlStateListener l) {
        Connection connection = null;
        CallableStatement callableStatement = null;
        try {
            connection = DatabaseBean.getConnection();
            callableStatement = connection.prepareCall("{call cancel_Reader_Borrow_Apply(?, ?)}");
            callableStatement.setString(1, b.getISBN());
            callableStatement.setString(2, r.getNo());
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
    public void cancelReturnApply(Reader r, Book b, SqlStateListener l) {
        Connection connection = null;
        CallableStatement callableStatement = null;
        try {
            connection = DatabaseBean.getConnection();
            callableStatement = connection.prepareCall("{call cancel_Reader_Return_Apply(?, ?)}");
            callableStatement.setString(1, b.getISBN());
            callableStatement.setString(2, r.getNo());
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
    public void bookRequest(Book book, Reader reader, SqlStateListener l) {
        Connection connection = null;
        CallableStatement callableStatement = null;

        try {
            connection = DatabaseBean.getConnection();
            callableStatement = connection.prepareCall("{call reader_book_request(?, ?, ?, ?, ?, ?)}");
            callableStatement.setString("ReaderNo", reader.getNo());
            callableStatement.setString("BookISBN", book.getISBN());
            callableStatement.setString("BookName", book.getName());
            callableStatement.setString("BookAuthor", book.getAuthor());
            callableStatement.setString("BookPublisher", book.getPublisher());
            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM");
            java.util.Date date1 = sDateFormat.parse(book.getPublishDate());
            Date date = new Date(date1.getTime());
            callableStatement.setDate("BookPublishDate", date);
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
    public Pair<List<Book_Request>, Integer> queryBookRequest(Reader r, int pageNow, int pageSize) throws Exception {
        int count;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Connection connection = DatabaseBean.getConnection();
        CallableStatement callableStatement = connection.prepareCall("{ ?=call query_Reader_Request_Book_History(?, ?, ?, ?)}");
        callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
        callableStatement.registerOutParameter(5, OracleTypes.NUMBER);
        callableStatement.setString(2, r.getNo());
        callableStatement.setInt(3, pageNow);
        callableStatement.setInt(4, pageSize);
        callableStatement.execute();
        ResultSet resultSet = (ResultSet) callableStatement.getObject(1);
        ArrayList<Book_Request> arrayList = new ArrayList<>();
        while (resultSet.next()){
            Book_Request history = new Book_Request();
            Book book = new Book();
            Staff staff = new Staff();
            book.setName(resultSet.getString("Name"));
            book.setISBN(resultSet.getString("ISBN"));
            book.setAuthor(resultSet.getString("Author"));
            book.setPublisher(resultSet.getString("Publisher"));
            staff.setNo(resultSet.getString("Staff_No"));
            Date date = resultSet.getDate("PublishDate");
            if (date == null)
                book.setPublishDate("-");
            else
                book.setPublishDate(sdf.format(date));
            history.setBook(book);
            history.setStaff(staff);
            history.setStatus(resultSet.getString("Status"));
            history.setReader(r);
            date = resultSet.getDate("Time");
            if (date == null)
                history.setTime("-");
            else
                history.setTime(sdf.format(date));
            arrayList.add(history);
        }
        count =  callableStatement.getInt(5);
        DatabaseBean.close(resultSet, callableStatement, connection);
        return new Pair<>(arrayList, count);
    }

    @Override
    public Pair<List<Book_Request>, Integer> queryBookRequestInWord(Reader r, String ISBN, int pageNow, int pageSize) throws Exception {
        int count;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Connection connection = DatabaseBean.getConnection();
        CallableStatement callableStatement = connection.prepareCall("{ ?=call query_Reader_Request_Book_History_With_Keyword(?, ?, ?, ?, ?)}");
        callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
        callableStatement.registerOutParameter(6, OracleTypes.NUMBER);
        callableStatement.setString(2, r.getNo());
        callableStatement.setString(3, ISBN);
        callableStatement.setInt(4, pageNow);
        callableStatement.setInt(5, pageSize);
        callableStatement.execute();
        ResultSet resultSet = (ResultSet) callableStatement.getObject(1);
        ArrayList<Book_Request> arrayList = new ArrayList<>();
        while (resultSet.next()){
            Book_Request history = new Book_Request();
            Book book = new Book();
            Staff staff = new Staff();
            book.setName(resultSet.getString("Name"));
            book.setISBN(resultSet.getString("ISBN"));
            book.setAuthor(resultSet.getString("Author"));
            book.setPublisher(resultSet.getString("Publisher"));
            staff.setNo(resultSet.getString("Staff_No"));
            Date date = resultSet.getDate("PublishDate");
            if (date == null)
                book.setPublishDate("-");
            else
                book.setPublishDate(sdf.format(date));
            history.setBook(book);
            history.setStaff(staff);
            history.setStatus(resultSet.getString("Status"));
            history.setReader(r);
            date = resultSet.getDate("Time");
            if (date == null)
                history.setTime("-");
            else
                history.setTime(sdf.format(date));
            arrayList.add(history);
        }
        count =  callableStatement.getInt(6);
        DatabaseBean.close(resultSet, callableStatement, connection);
        return new Pair<>(arrayList, count);
    }

    @Override
    public void cancelBookRequest(Reader r, Book b, SqlStateListener l) {
        Connection connection = null;
        CallableStatement callableStatement = null;
        try {
            connection = DatabaseBean.getConnection();
            callableStatement = connection.prepareCall("{call cancel_Book_Request(?, ?)}");
            callableStatement.setString(1, b.getISBN());
            callableStatement.setString(2, r.getNo());
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
    public void setMessage(Reader reader, String time, SqlStateListener l) {
        Connection connection = null;
        CallableStatement callableStatement = null;
        try {
            connection = DatabaseBean.getConnection();
            callableStatement = connection.prepareCall("{call set_Reader_Message(?, ?)}");
            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            java.util.Date date1 = sDateFormat.parse(time);
            Date date = new Date(date1.getTime());
            callableStatement.setDate("t", date);
            callableStatement.setString("No", reader.getNo());
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

    private ArrayList<Reader_Borrow_Return_History> getHistory(ResultSet resultSet) throws SQLException {
        ArrayList<Reader_Borrow_Return_History> arrayList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        while (resultSet.next()) {
            Reader_Borrow_Return_History readerBorrowReturnHistory = new Reader_Borrow_Return_History();
            Reader reader = new Reader();
            Book book = new Book();
            reader.setNo(resultSet.getString("No"));
            book.setISBN(resultSet.getString("ISBN"));
            book.setCover(resultSet.getString("Cover"));
            book.setName(resultSet.getString("Name"));
            readerBorrowReturnHistory.setReader(reader);
            readerBorrowReturnHistory.setBook(book);
            readerBorrowReturnHistory.setBorrowDate(sdf.format(resultSet.getDate("BorrowDate")));
            readerBorrowReturnHistory.setShouldReturnDate(sdf.format(resultSet.getDate("ShouldReturnDate")));
            Date date = resultSet.getDate("ReturnDate");
            if (date == null)
                readerBorrowReturnHistory.setReturnDate("-");
            else
                readerBorrowReturnHistory.setReturnDate(sdf.format(resultSet.getDate(5)));
            arrayList.add(readerBorrowReturnHistory);
        }
        return arrayList;
    }

    private void fillPara_6(CallableStatement callableStatement, Reader a) throws SQLException {
        callableStatement.setString(1, a.getNo());
        callableStatement.setString(2, a.getPassword());
        callableStatement.setString(3, a.getName());
        callableStatement.setString(4, a.getGender());
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
        reader.setNo(resultSet.getString("No"));
        reader.setName(resultSet.getString("Name"));
        reader.setGender(resultSet.getString("Gender"));
        reader.setTakeEffectDate(sdf.format(resultSet.getDate("TakeEffectDate")));
        Date date = resultSet.getDate("LoseEffectDate");
        if (date == null)
            reader.setLoseEffectDate("-");
        else
            reader.setLoseEffectDate(sdf.format(resultSet.getDate("LoseEffectDate")));
        return reader;
    }

    private ArrayList<Reader> getReader(ResultSet resultSet) throws SQLException {
        ArrayList<Reader> arrayList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        while (resultSet.next()) {
            Reader reader = new Reader();
            reader.setNo(resultSet.getString("No"));
            reader.setName(resultSet.getString("Name"));
            reader.setGender(resultSet.getString("Gender"));
            reader.setTakeEffectDate(sdf.format(resultSet.getDate("TakeEffectDate")));
            Date date = resultSet.getDate("LoseEffectDate");
            if (date == null)
                reader.setLoseEffectDate("-");
            else
                reader.setLoseEffectDate(sdf.format(resultSet.getDate("LoseEffectDate")));
            arrayList.add(reader);
        }
        return arrayList;
    }
}
