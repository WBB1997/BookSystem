package dao;

import javafx.util.Pair;
import model.*;
import util.SqlStateListener;

import java.sql.SQLException;
import java.util.List;

public interface IStaffDao {
    // 注册账号
    void registerAccount(Staff s, SqlStateListener l);

    // 修改账号信息
    void modAccount(Staff s, SqlStateListener l);

    // 删除账号信息
    void delAccount(Staff s, SqlStateListener l);

    // 检查账号是否存在
    boolean checkStaffAccount(Staff s) throws Exception;

    // 核对账号密码
    boolean checkStaff(Staff s) throws Exception;

    // 获得历史记录
    Pair<List<Staff_DealWith_Book_History>, Integer> queryHistory(Staff s, int pageNow, int pageSize) throws Exception;

    // 获得有关键词历史记录
    Pair<List<Staff_DealWith_Book_History>, Integer> queryHistoryInWord(Staff s, String ISBN, int pageNow, int pageSize) throws Exception;

    // 获得Staff列表
    Pair<List<Staff>, Integer> getStaffList(int pageNow, int pageSize) throws SQLException;

    // 获得Staff列表
    Pair<List<Staff>, Integer> getStaffListInKeyWord(String keyword, int pageNow, int pageSize) throws SQLException;

    // 获取个人详情
    Staff getStaffDetails(Staff s) throws SQLException;

    // 获取当前所有的借书申请
    Pair<List<Staff_DealWith_Reader_Borrow_History>, Integer> getStaffDealWithReaderBorrowHistory(int pageNow, int pageSize) throws SQLException;

    // 获取当前所有的借书申请带关键词
    Pair<List<Staff_DealWith_Reader_Borrow_History>, Integer> getStaffDealWithReaderBorrowHistoryWithWord(String keyword, int pageNow, int pageSize) throws SQLException;


    // 获取当前所有的还书申请
    Pair<List<Staff_DealWith_Reader_Return_History>, Integer> getStaffDealWithReaderReturnHistory(int pageNow, int pageSize) throws SQLException;

    // 获取当前所有的还书申请带关键词
    Pair<List<Staff_DealWith_Reader_Return_History>, Integer> getStaffDealWithReaderReturnHistoryWithWord(String keyword, int pageNow, int pageSize) throws SQLException;

    // 处理借书请求
    void dealWithBorrowBook(String keyword, Book book, Staff staff, Reader reader, SqlStateListener l);

    // 处理还书请求
    void dealWithReturnBook(String keyword, Book book, Staff staff, Reader reader, SqlStateListener l);

    // 获取当前所有的荐购申请
    Pair<List<Book_Request>, Integer> getBookRequest(int pageNow, int pageSize) throws SQLException;

    // 获取当前所有的荐购申请带关键词
    Pair<List<Book_Request>, Integer> getBookRequestWithWord(String keyword, int pageNow, int pageSize) throws SQLException;

    // 处理借书请求
    void dealWithBookRequest(String keyword, Book book, Staff staff, Reader reader, SqlStateListener l);
}
