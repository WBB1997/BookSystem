package dao.impl;

import dao.IStaffDao;
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

public class StaffDaoImpl implements IStaffDao {
    @Override
    public void registerAccount(Staff s, SqlStateListener l) {
        Connection connection = null;
        CallableStatement callableStatement = null;
        try {
            connection = DatabaseBean.getConnection();
            callableStatement = connection.prepareCall("{call register_account_of_staff(?, ?, ?, ?, ?)}");
            fillPara(callableStatement, s);
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
    public void modAccount(Staff s, SqlStateListener l) {
        Connection connection = null;
        CallableStatement callableStatement = null;
        try {
            connection = DatabaseBean.getConnection();
            callableStatement = connection.prepareCall("{call mod_Account_Of_Staff(?, ?, ?, ?, ?)}");
            fillPara(callableStatement, s);
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
    public void delAccount(Staff s, SqlStateListener l) {
        Connection connection = null;
        CallableStatement callableStatement = null;
        try {
            connection = DatabaseBean.getConnection();
            callableStatement = connection.prepareCall("{call del_Account_Of_Staff(?)}");
            callableStatement.setString(1, s.getNo());
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
    public boolean checkStaffAccount(Staff s) throws Exception {
        int flag;
        Connection connection = DatabaseBean.getConnection();
        CallableStatement callableStatement = connection.prepareCall("{ ?=call Check_Staff_Account(?)}");
        callableStatement.registerOutParameter(1, OracleTypes.NUMBER);
        callableStatement.setString(2, s.getNo());
        callableStatement.execute();
        flag = callableStatement.getInt(1);
        DatabaseBean.close(null, callableStatement, connection);
        return flag != 0;
    }

    @Override
    public boolean checkStaff(Staff s) throws Exception {
        int flag;
        Connection connection = DatabaseBean.getConnection();
        CallableStatement callableStatement = connection.prepareCall("{ ?=call Check_Staff(?, ?)}");
        callableStatement.registerOutParameter(1, OracleTypes.NUMBER);
        callableStatement.setString(2, s.getNo());
        callableStatement.setString(3, s.getPassword());
        callableStatement.execute();
        flag = callableStatement.getInt(1);
        DatabaseBean.close(null, callableStatement, connection);
        return flag != 0;
    }

    @Override
    public Pair<List<Staff_DealWith_Book_History>, Integer> queryHistory(Staff s, int pageNow, int pageSize) throws Exception {
        int count;
        Connection connection = DatabaseBean.getConnection();
        CallableStatement callableStatement = connection.prepareCall("{ ?=call query_staff_history(?, ?, ?, ?, ?)}");
        fillPara_6(callableStatement,s,pageNow,pageSize);
        callableStatement.execute();
        ResultSet resultSet = (ResultSet) callableStatement.getObject(1);
        ArrayList<Staff_DealWith_Book_History> arrayList = getHistory(resultSet);
        count =  callableStatement.getInt(6);
        DatabaseBean.close(resultSet, callableStatement, connection);
        return new Pair<>(arrayList, count);
    }

    @Override
    public Pair<List<Staff_DealWith_Book_History>, Integer> queryHistoryInWord(Staff s, String ISBN, int pageNow, int pageSize) throws Exception {
        int count;
        Connection connection = DatabaseBean.getConnection();
        CallableStatement callableStatement = connection.prepareCall("{ ?=call query_staff_history_in_isbn(?, ?, ?, ?, ?, ?)}");
        callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
        callableStatement.registerOutParameter(7, OracleTypes.NUMBER);
        callableStatement.setString(2, s.getNo());
        callableStatement.setString(3, s.getPassword());
        callableStatement.setString(4, ISBN);
        callableStatement.setInt(5, pageNow);
        callableStatement.setInt(6, pageSize);
        callableStatement.execute();
        ResultSet resultSet = (ResultSet) callableStatement.getObject(1);
        ArrayList<Staff_DealWith_Book_History> arrayList = getHistory(resultSet);
        count =  callableStatement.getInt(7);
        DatabaseBean.close(resultSet, callableStatement, connection);
        return new Pair<>(arrayList, count);
    }

    @Override
    public Pair<List<Staff>, Integer> getStaffList(int pageNow, int pageSize) throws SQLException {
        int count;
        Connection connection = DatabaseBean.getConnection();
        CallableStatement callableStatement = connection.prepareCall("{ ?=call query_staff_list(?, ?, ?)}");
        callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
        callableStatement.registerOutParameter(4, OracleTypes.NUMBER);
        callableStatement.setInt(2, pageNow);
        callableStatement.setInt(3, pageSize);
        callableStatement.execute();
        ResultSet resultSet = (ResultSet) callableStatement.getObject(1);
        count =  callableStatement.getInt(4);
        ArrayList<Staff> arrayList = getStaff(resultSet);
        DatabaseBean.close(resultSet, callableStatement, connection);
        return new Pair<>(arrayList, count);
    }

    @Override
    public Pair<List<Staff>, Integer> getStaffListInKeyWord(String keyword, int pageNow, int pageSize) throws SQLException {
        int count;
        Connection connection = DatabaseBean.getConnection();
        CallableStatement callableStatement = connection.prepareCall("{ ?=call query_staff_list_in_keyword(?, ?, ?, ?)}");
        callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
        callableStatement.registerOutParameter(5, OracleTypes.NUMBER);
        callableStatement.setString(2, keyword);
        callableStatement.setInt(3, pageNow);
        callableStatement.setInt(4, pageSize);
        callableStatement.execute();
        ResultSet resultSet = (ResultSet) callableStatement.getObject(1);
        count =  callableStatement.getInt(5);
        ArrayList<Staff> arrayList = getStaff(resultSet);
        DatabaseBean.close(resultSet, callableStatement, connection);
        return new Pair<>(arrayList, count);
    }

    @Override
    public Staff getStaffDetails(Staff s) throws SQLException {
        Connection connection;
        CallableStatement callableStatement;
        connection = DatabaseBean.getConnection();
        callableStatement = connection.prepareCall("{ ? = call get_Staff_Details(?, ?)}");
        callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
        callableStatement.setString(2, s.getNo());
        callableStatement.setString(3, s.getPassword());
        callableStatement.execute();
        ResultSet resultSet = (ResultSet) callableStatement.getObject(1);
        Staff staffDetails = fillStaff(resultSet);
        DatabaseBean.close(null, callableStatement, connection);
        return staffDetails;
    }

    @Override
    public Pair<List<Staff_DealWith_Reader_Borrow_History>, Integer> getStaffDealWithReaderBorrowHistory(int pageNow, int pageSize) throws SQLException {
        int count;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Connection connection = DatabaseBean.getConnection();
        CallableStatement callableStatement = connection.prepareCall("{ ?=call get_Staff_DealWith_Reader_Borrow_History(?, ?, ?)}");
        callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
        callableStatement.registerOutParameter(4, OracleTypes.NUMBER);
        callableStatement.setInt(2, pageNow);
        callableStatement.setInt(3, pageSize);
        callableStatement.execute();
        ResultSet resultSet = (ResultSet) callableStatement.getObject(1);
        ArrayList<Staff_DealWith_Reader_Borrow_History> arrayList = new ArrayList<>();
        while (resultSet.next()){
            Staff_DealWith_Reader_Borrow_History history = new Staff_DealWith_Reader_Borrow_History();
            Book book = new Book();
            Staff staff = new Staff();
            Reader reader = new Reader();
            book.setName(resultSet.getString("Name"));
            book.setISBN(resultSet.getString("ISBN"));
            book.setCover(resultSet.getString("Cover"));
            staff.setNo(resultSet.getString("Staff_No"));
            reader.setNo(resultSet.getString("Reader_No"));
            history.setBook(book);
            history.setStaff(staff);
            history.setStatus(resultSet.getString("Status"));
            history.setReader(reader);
            Date date = resultSet.getDate("Time");
            if (date == null)
                history.setTime("-");
            else
                history.setTime(sdf.format(resultSet.getDate("Time")));
            arrayList.add(history);
        }
        count =  callableStatement.getInt(4);
        DatabaseBean.close(resultSet, callableStatement, connection);
        return new Pair<>(arrayList, count);
    }

    @Override
    public Pair<List<Staff_DealWith_Reader_Borrow_History>, Integer> getStaffDealWithReaderBorrowHistoryWithWord(String keyword, int pageNow, int pageSize) throws SQLException {
        int count;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Connection connection = DatabaseBean.getConnection();
        CallableStatement callableStatement = connection.prepareCall("{ ?=call get_Staff_DealWith_Reader_Borrow_History_With_Keyword(?, ?, ?, ?)}");
        callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
        callableStatement.registerOutParameter(5, OracleTypes.NUMBER);
        callableStatement.setString(2, keyword);
        callableStatement.setInt(3, pageNow);
        callableStatement.setInt(4, pageSize);
        callableStatement.execute();
        ResultSet resultSet = (ResultSet) callableStatement.getObject(1);
        ArrayList<Staff_DealWith_Reader_Borrow_History> arrayList = new ArrayList<>();
        while (resultSet.next()){
            Staff_DealWith_Reader_Borrow_History history = new Staff_DealWith_Reader_Borrow_History();
            Book book = new Book();
            Staff staff = new Staff();
            Reader reader = new Reader();
            book.setName(resultSet.getString("Name"));
            book.setISBN(resultSet.getString("ISBN"));
            book.setCover(resultSet.getString("Cover"));
            staff.setNo(resultSet.getString("Staff_No"));
            reader.setNo(resultSet.getString("Reader_No"));
            history.setBook(book);
            history.setStaff(staff);
            history.setStatus(resultSet.getString("Status"));
            history.setReader(reader);
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
    public Pair<List<Staff_DealWith_Reader_Return_History>, Integer> getStaffDealWithReaderReturnHistory(int pageNow, int pageSize) throws SQLException {
        int count;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Connection connection = DatabaseBean.getConnection();
        CallableStatement callableStatement = connection.prepareCall("{ ?=call get_Staff_DealWith_Reader_Return_History(?, ?, ?)}");
        callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
        callableStatement.registerOutParameter(4, OracleTypes.NUMBER);
        callableStatement.setInt(2, pageNow);
        callableStatement.setInt(3, pageSize);
        callableStatement.execute();
        ResultSet resultSet = (ResultSet) callableStatement.getObject(1);
        ArrayList<Staff_DealWith_Reader_Return_History> arrayList = new ArrayList<>();
        while (resultSet.next()){
            Staff_DealWith_Reader_Return_History history = new Staff_DealWith_Reader_Return_History();
            Book book = new Book();
            Staff staff = new Staff();
            Reader reader = new Reader();
            book.setName(resultSet.getString("Name"));
            book.setISBN(resultSet.getString("ISBN"));
            book.setCover(resultSet.getString("Cover"));
            staff.setNo(resultSet.getString("Staff_No"));
            reader.setNo(resultSet.getString("Reader_No"));
            history.setBook(book);
            history.setStaff(staff);
            history.setStatus(resultSet.getString("Status"));
            history.setReader(reader);
            Date date = resultSet.getDate("Time");
            if (date == null)
                history.setTime("-");
            else
                history.setTime(sdf.format(resultSet.getDate("Time")));
            arrayList.add(history);
        }
        count =  callableStatement.getInt(4);
        DatabaseBean.close(resultSet, callableStatement, connection);
        return new Pair<>(arrayList, count);
    }

    @Override
    public Pair<List<Staff_DealWith_Reader_Return_History>, Integer> getStaffDealWithReaderReturnHistoryWithWord(String keyword, int pageNow, int pageSize) throws SQLException {
        int count;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Connection connection = DatabaseBean.getConnection();
        CallableStatement callableStatement = connection.prepareCall("{ ?=call get_Staff_DealWith_Reader_Return_History_With_Keyword(?, ?, ?, ?)}");
        callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
        callableStatement.registerOutParameter(5, OracleTypes.NUMBER);
        callableStatement.setString(2, keyword);
        callableStatement.setInt(3, pageNow);
        callableStatement.setInt(4, pageSize);
        callableStatement.execute();
        ResultSet resultSet = (ResultSet) callableStatement.getObject(1);
        ArrayList<Staff_DealWith_Reader_Return_History> arrayList = new ArrayList<>();
        while (resultSet.next()){
            Staff_DealWith_Reader_Return_History history = new Staff_DealWith_Reader_Return_History();
            Book book = new Book();
            Staff staff = new Staff();
            Reader reader = new Reader();
            book.setName(resultSet.getString("Name"));
            book.setISBN(resultSet.getString("ISBN"));
            book.setCover(resultSet.getString("Cover"));
            staff.setNo(resultSet.getString("Staff_No"));
            reader.setNo(resultSet.getString("Reader_No"));
            history.setBook(book);
            history.setStaff(staff);
            history.setStatus(resultSet.getString("Status"));
            history.setReader(reader);
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
    public void dealWithBorrowBook(String keyword, Book book, Staff staff, Reader reader, SqlStateListener l) {
        Connection connection = null;
        CallableStatement callableStatement = null;
        try {
            connection = DatabaseBean.getConnection();
            callableStatement = connection.prepareCall("{call DealWith_Borrow_Book(?, ?, ?, ?)}");
            callableStatement.setString("parameter", keyword);
            callableStatement.setString("BookISBN", book.getISBN());
            callableStatement.setString("ReaderNo", reader.getNo());
            callableStatement.setString("StaffNo", staff.getNo());
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
    public void dealWithReturnBook(String keyword, Book book, Staff staff, Reader reader, SqlStateListener l) {
        Connection connection = null;
        CallableStatement callableStatement = null;
        try {
            connection = DatabaseBean.getConnection();
            callableStatement = connection.prepareCall("{call DealWith_Return_Book(?, ?, ?, ?)}");
            callableStatement.setString("parameter", keyword);
            callableStatement.setString("BookISBN", book.getISBN());
            callableStatement.setString("ReaderNo", reader.getNo());
            callableStatement.setString("StaffNo", staff.getNo());
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
    public Pair<List<Book_Request>, Integer> getBookRequest(int pageNow, int pageSize) throws SQLException {
        int count;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Connection connection = DatabaseBean.getConnection();
        CallableStatement callableStatement = connection.prepareCall("{ ?=call get_Reader_Request_Book_History(?, ?, ?)}");
        callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
        callableStatement.registerOutParameter(4, OracleTypes.NUMBER);
        callableStatement.setInt(2, pageNow);
        callableStatement.setInt(3, pageSize);
        callableStatement.execute();
        ResultSet resultSet = (ResultSet) callableStatement.getObject(1);
        ArrayList<Book_Request> arrayList = new ArrayList<>();
        while (resultSet.next()){
            Book_Request history = new Book_Request();
            Book book = new Book();
            Staff staff = new Staff();
            Reader reader = new Reader();
            reader.setNo(resultSet.getString("No"));
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
            history.setReader(reader);
            date = resultSet.getDate("Time");
            if (date == null)
                history.setTime("-");
            else
                history.setTime(sdf.format(date));
            arrayList.add(history);
        }
        count =  callableStatement.getInt(4);
        DatabaseBean.close(resultSet, callableStatement, connection);
        return new Pair<>(arrayList, count);
    }

    @Override
    public Pair<List<Book_Request>, Integer> getBookRequestWithWord(String keyword, int pageNow, int pageSize) throws SQLException {
        int count;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Connection connection = DatabaseBean.getConnection();
        CallableStatement callableStatement = connection.prepareCall("{ ?=call get_Reader_Request_Book_History_With_Keyword(?, ?, ?, ?)}");
        callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
        callableStatement.registerOutParameter(5, OracleTypes.NUMBER);
        callableStatement.setString(2, keyword);
        callableStatement.setInt(3, pageNow);
        callableStatement.setInt(4, pageSize);
        callableStatement.execute();
        ResultSet resultSet = (ResultSet) callableStatement.getObject(1);
        ArrayList<Book_Request> arrayList = new ArrayList<>();
        while (resultSet.next()){
            Book_Request history = new Book_Request();
            Book book = new Book();
            Staff staff = new Staff();
            Reader reader = new Reader();
            reader.setNo(resultSet.getString("No"));
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
            history.setReader(reader);
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
    public void dealWithBookRequest(String keyword, Book book, Staff staff, Reader reader, SqlStateListener l) {
        Connection connection = null;
        CallableStatement callableStatement = null;
        try {
            connection = DatabaseBean.getConnection();
            callableStatement = connection.prepareCall("{call DealWith_Book_Request(?, ?, ?, ?)}");
            callableStatement.setString("parameter", keyword);
            callableStatement.setString("BookISBN", book.getISBN());
            callableStatement.setString("ReaderNo", reader.getNo());
            callableStatement.setString("StaffNo", staff.getNo());
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

    private ArrayList<Staff_DealWith_Book_History> getHistory(ResultSet resultSet) throws SQLException {
        ArrayList<Staff_DealWith_Book_History> arrayList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        while (resultSet.next()) {
            Staff_DealWith_Book_History staffHistory = new Staff_DealWith_Book_History();
            Book book = new Book();
            Staff staff = new Staff();
            staff.setNo(resultSet.getString("NO"));
            book.setISBN(resultSet.getString("ISBN"));
            book.setName(resultSet.getString("Name"));
            book.setCover(resultSet.getString("Cover"));
            staffHistory.setPurchaseDate(sdf.format(resultSet.getDate("PurchaseDate")));
            staffHistory.setPurchaseAmount(resultSet.getInt("PurchaseAmount"));
            staffHistory.setBook(book);
            staffHistory.setStaff(staff);
            arrayList.add(staffHistory);
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

    private Staff fillStaff(ResultSet resultSet) throws SQLException {
        Staff staff = new Staff();
        resultSet.next();
        staff.setNo(resultSet.getString(1));
        staff.setPassword(resultSet.getString(2));
        staff.setName(resultSet.getString(3));
        staff.setGender(resultSet.getString(4));
        staff.setPhone(resultSet.getString(5));
        return staff;
    }


    private void fillPara(CallableStatement callableStatement, Staff s) throws SQLException {
        callableStatement.setString(1, s.getNo());
        callableStatement.setString(2, s.getPassword());
        callableStatement.setString(3, s.getName());
        callableStatement.setString(4, s.getGender());
        callableStatement.setString(5, s.getPhone());
    }

    private void fillPara_6(CallableStatement callableStatement, Staff s, int pageNow, int pageSize) throws SQLException {
        callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
        callableStatement.registerOutParameter(6, OracleTypes.NUMBER);
        callableStatement.setString(2, s.getNo());
        callableStatement.setString(3, s.getPassword());
        callableStatement.setInt(4, pageNow);
        callableStatement.setInt(5, pageSize);
    }

    private ArrayList<Staff> getStaff(ResultSet resultSet) throws SQLException {
        ArrayList<Staff> arrayList = new ArrayList<>();
        while (resultSet.next()) {
            Staff staff = new Staff();
            staff.setNo(resultSet.getString("No"));
            staff.setName(resultSet.getString("Name"));
            staff.setGender(resultSet.getString("Gender"));
            staff.setPhone(resultSet.getString("Phone"));
            arrayList.add(staff);
        }
        return arrayList;
    }
}
