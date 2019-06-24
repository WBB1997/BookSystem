package dao;

import model.Book;
import model.Reader;
import util.SqlStateListener;

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
}
