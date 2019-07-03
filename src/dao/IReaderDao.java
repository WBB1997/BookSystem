package dao;

import javafx.util.Pair;
import model.Book;
import model.Reader;
import model.Reader_Borrow_Return_History;
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

    // 获得历史记录
    Pair<List<Reader_Borrow_Return_History>, Integer> queryHistory(Reader r, int pageNow, int pageSize) throws Exception;

    // 获得有关键词历史记录
    Pair<List<Reader_Borrow_Return_History>, Integer> queryHistoryInWord(Reader r, String ISBN, int pageNow, int pageSize) throws Exception;

    // 查询读者类型
    List<String> getReaderTypes() throws SQLException;

    // 查询读者类型
    List<String> getCollegeTypes() throws SQLException;
}
