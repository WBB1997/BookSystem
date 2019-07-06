package dao;

import javafx.util.Pair;
import model.Admin;
import model.Book;
import model.Reader;
import model.Staff;
import util.SqlStateListener;

import java.sql.SQLException;
import java.util.List;

public interface IBookDao {
    // 增加
    void addBook(Staff staff, Book b, SqlStateListener l);

    // 添加
    void increaseBook(Book b, int quantity, Staff staff, SqlStateListener l);

    // 删除图书
    void decreaseBook(Book b, int quantity, Staff staff, SqlStateListener l);

    // 修改图书
    void modBook(Book b, Staff staff, SqlStateListener l);

    // 通过ISBN查询图书是否存在(精确查询)
    boolean checkBookOfISBN(Book b) throws Exception;

    // 通过ISBN和读者账号确认读者是否借过此书
    boolean isReaderBorrowBook(Book b, Reader r) throws Exception;

    // 任意词查询(模糊查询)
    Pair<List<Book>, Integer> queryAllBook(String word, int pageNow, int pageSize) throws Exception;

    // 任意字段查询(模糊查询)
    Pair<List<Book>, Integer> queryBookOfKeyword(String parameter, String word, int pageNow, int pageSize) throws Exception;

    // 获取书籍类型
    List<String> getBookTypes() throws SQLException;
}
