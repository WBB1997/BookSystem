package dao;

import javafx.util.Pair;
import model.*;
import util.SqlStateListener;

import java.sql.SQLException;
import java.util.List;

public interface IReaderDao {
    // 注册账号
    void registerAccount(Reader r, SqlStateListener l);

    // 修改账号
    void modAccount(Reader r, SqlStateListener l);

    // 删除账号
    void delAccount(Reader r, SqlStateListener l);

    // 检查账号是否存在
    boolean checkReaderAccount(Reader r) throws Exception;

    // 核对账号密码
    boolean checkReader(Reader r) throws Exception;

    // 借书
    void borrowBook(Reader r, Book b, SqlStateListener l);

    // 归还
    void returnBook(Reader r, Book b, SqlStateListener l);

    // 获得个人详情
    Reader getReaderDetails(Reader r) throws SQLException;

    // 获得读者列表
    Pair<List<Reader>, Integer> getReaderList(int pageNow, int pageSize) throws SQLException;

    // 获得读者列表
    Pair<List<Reader>, Integer> getReaderListInKeyWord(String keyword, int pageNow, int pageSize) throws SQLException;

    // 获得历史记录
    Pair<List<Reader_Borrow_Return_History>, Integer> queryHistory(Reader r, int pageNow, int pageSize) throws Exception;

    // 获得有关键词历史记录
    Pair<List<Reader_Borrow_Return_History>, Integer> queryHistoryInWord(Reader r, String ISBN, int pageNow, int pageSize) throws Exception;

    // 获得借书申请
    Pair<List<Staff_DealWith_Reader_Borrow_History>, Integer> queryStaffDealWithReaderBorrowHistory(Reader r, int pageNow, int pageSize) throws Exception;

    // 获得借书申请带关键词
    Pair<List<Staff_DealWith_Reader_Borrow_History>, Integer> queryStaffDealWithReaderBorrowHistoryInWord(Reader r, String ISBN, int pageNow, int pageSize) throws Exception;

    // 获得还书申请
    Pair<List<Staff_DealWith_Reader_Return_History>, Integer> queryStaffDealWithReaderReturnHistory(Reader r, int pageNow, int pageSize) throws Exception;

    // 获得还书申请带关键词
    Pair<List<Staff_DealWith_Reader_Return_History>, Integer> queryStaffDealWithReaderReturnHistoryInWord(Reader r, String ISBN, int pageNow, int pageSize) throws Exception;

    // 获得消息列表
    Pair<List<Reader_Message>, Integer>  getMessageList(Reader r, int pageNow, int pageSize) throws SQLException;

    // 取消借阅申请
    void cancelBorrowApply(Reader r, Book b, SqlStateListener l);

    // 取消归还申请
    void cancelReturnApply(Reader r, Book b, SqlStateListener l);

    // 书籍荐购
    void bookRequest(Book book, Reader reader, SqlStateListener l);

    // 获取读者的荐购信息
    Pair<List<Book_Request>, Integer> queryBookRequest(Reader r, int pageNow, int pageSize) throws Exception;

    // 获取读者的荐购申请带关键词
    Pair<List<Book_Request>, Integer> queryBookRequestInWord(Reader r, String ISBN, int pageNow, int pageSize) throws Exception;

    // 取消荐购申请
    void cancelBookRequest(Reader r, Book b, SqlStateListener l);

    // 已读消息
    void setMessage(Reader reader, String time ,SqlStateListener l);

    // 读者预约
    void bookSubscribe(Reader r, Book b, SqlStateListener l);

    // 取消读者预约
    void cancelBookSubscribe(Reader r, Book b, SqlStateListener l);

    // 查询读者的所有预约
    Pair<List<Book_Subscribe>, Integer> queryBookSubscribe(Reader r, int pageNow, int pageSize) throws Exception;

    // 获取读者的所有预约申请带关键词
    Pair<List<Book_Subscribe>, Integer> queryBookSubscribeInWord(Reader r, String ISBN, int pageNow, int pageSize) throws Exception;

    // 续借
    void continueBorrow(Reader r, Book b, SqlStateListener l);
}
