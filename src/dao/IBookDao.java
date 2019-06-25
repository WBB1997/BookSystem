package dao;

import model.Admin;
import model.Book;
import model.Reader;
import util.SqlStateListener;

import java.util.List;

public interface IBookDao {
    // 增加
    void addBook(Admin a, Book b, SqlStateListener l);

    // 添加
    void increaseBook(Book b, int quantity, Admin a, SqlStateListener l);

    // 删除图书
    void decreaseBook(Book b, int quantity, Admin a, SqlStateListener l);

    // 修改图书
    void modBook(Book b, Admin a, SqlStateListener l);

    // 通过ISBN查询图书是否存在(精确查询)
    boolean checkBookOfISBN(Book b) throws Exception;

    // 通过ISBN和读者账号确认读者是否借过此书
    boolean isReaderBorrowBook(Book b, Reader r) throws Exception;

    // 任意词查询(模糊查询)
    List<Book> queryAllBook(String word, int pageNow, int pageSize) throws Exception;

    // 任意字段查询(模糊查询)
    List<Book> queryBookOfKeyword(String parameter, String word, int pageNow, int pageSize) throws Exception;

    // 查询图书的总数
    int countBook() throws Exception;
}
