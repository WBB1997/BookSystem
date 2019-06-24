package test;

import model.Admin;
import model.Book;
import model.Reader;
import util.DaoFactory;
import util.SqlStateListener;

import java.util.ArrayList;
import java.util.List;

public class databaseTest {
    public static void main(String[] args) {

        // 管理员注册测试
        // 测试数据1， 账户名重复 错误代码：ORA-00001
//        Admin admin = new Admin();
//        admin.setNo("690252189");
//        admin.setPassword("a690252189");
//        admin.setName("吴贝贝");
//        admin.setGender("男");
//        admin.setPhone("18372637615");
//        DaoFactory.getAdminDao().registerAccount(admin, new SqlStateListener() {
//            @Override
//            public void Error(int ErrorCode, String ErrorMessage) {
//                System.out.println(ErrorMessage);
//            }
//            @Override
//            public void Correct() {
//                System.out.println("注册成功");
//            }
//        });

        // 测试数据2：数据长度超出 错误代码：ORA-12899
//        Admin admin = new Admin();
//        admin.setNo("690252189");
//        admin.setPassword("a69025210000000000000000000000000000000000000000000000089");
//        admin.setName("吴贝贝");
//        admin.setGender("男");
//        admin.setPhone("18372637615");
//        DaoFactory.getAdminDao().registerAccount(admin, new SqlStateListener() {
//            @Override
//            public void Error(int ErrorCode, String ErrorMessage) {
//                System.out.println(ErrorMessage);
//            }
//            @Override
//            public void Correct() {
//
//            }
//        });

        // 测试数据3：性别不是‘男’或‘女’ 错误代码：ORA-02290
//        Admin admin = new Admin();
//        admin.setNo("690252189");
//        admin.setPassword("a690252189");
//        admin.setName("吴贝贝");
//        admin.setGender("公");
//        admin.setPhone("18372637615");
//        DaoFactory.getAdminDao().registerAccount(admin, new SqlStateListener() {
//            @Override
//            public void Error(int ErrorCode, String ErrorMessage) {
//                System.out.println(ErrorMessage);
//            }
//            @Override
//            public void Correct() {
//
//            }
//        });

        // 测试数据4：正常插入
//        Admin admin = new Admin();
//        admin.setNo("1448111756");
//        admin.setPassword("a6902521089");
//        admin.setName("吴贝贝");
//        admin.setGender("男");
//        admin.setPhone("18372637615");
//        DaoFactory.getAdminDao().registerAccount(admin, new SqlStateListener() {
//            @Override
//            public void Error(int ErrorCode, String ErrorMessage) {
//                System.out.println(ErrorMessage);
//            }
//
//            @Override
//            public void Correct() {
//                System.out.println("插入成功");
//            }
//        });

        // 管理员资料修改测试
//        Admin admin = new Admin();
//        admin.setNo("1448111756");
//        admin.setPassword("690252189");
//        admin.setName("吴贝贝");
//        admin.setGender("男");
//        admin.setPhone("18372637615");
//        DaoFactory.getAdminDao().modAccount(admin, new SqlStateListener() {
//            @Override
//            public void Error(int ErrorCode, String ErrorMessage) {
//                System.out.println(ErrorMessage);
//            }
//
//            @Override
//            public void Correct() {
//                System.out.println("更新成功");
//            }
//        });

        // 管理员资料删除测试
//        Admin admin = new Admin();
//        admin.setNo("1448111756");
//        admin.setPassword("6902189");
//        DaoFactory.getAdminDao().delAccount(admin, new SqlStateListener() {
//            @Override
//            public void Error(int ErrorCode, String ErrorMessage) {
//                System.out.println(ErrorMessage);
//            }
//
//            @Override
//            public void Correct() {
//                System.out.println("删除成功");
//            }
//        });

        // 插入图书
//        Admin admin = new Admin();
//        admin.setNo("690252189");
//        admin.setPassword("a690252189");
//        Book b = new Book();
//        b.setISBN("9787040441");
//        b.setName("数据库系统概论(第五版)");
//        b.setAuthor("王珊 萨师煊");
//        b.setPublisher("高等教育出版社");
//        b.setPublishDate("2014-09");
//         DaoFactory.getBookDao().AddBook(admin, b, new SqlStateListener() {
//            @Override
//            public void Error(int ErrorCode, String ErrorMessage) {
//                System.out.println(ErrorMessage);
//            }
//
//            @Override
//            public void Correct() {
//                System.out.println("添加成功");
//            }
//        });

        // 删除图书
//        Admin admin = new Admin();
//        admin.setNo("690252189");
//        admin.setPassword("a690252189");
//        Book b = new Book();
//        b.setISBN("9787040441");
//        b.setName("数据库系统概论(第五版)");
//        b.setAuthor("王珊 萨师煊");
//        b.setPublisher("高等教育出版社");
//        b.setPublishDate("2014-09");
//        DaoFactory.getBookDao().DecreaseBook(b, 5, admin, new SqlStateListener() {
//            @Override
//            public void Error(int ErrorCode, String ErrorMessage) {
//                System.out.println(ErrorMessage);
//            }
//
//            @Override
//            public void Correct() {
//                System.out.println("删除成功");
//            }
//        });

        // 修改图书
//        Admin admin = new Admin();
//        admin.setNo("690252189");
//        admin.setPassword("a690252189");
//        Book b = new Book();
//        b.setISBN("9787040441");
//        b.setName("数据库系统概论(第六版)");
//        b.setAuthor("王珊 萨师煊");
//        b.setPublisher("高等教育出版社");
//        b.setPublishDate("2014-09");
//        b.setAmount(-5);
//        DaoFactory.getBookDao().ModBook(b, admin, new SqlStateListener() {
//            @Override
//            public void Error(int ErrorCode, String ErrorMessage) {
//                System.out.println(ErrorMessage);
//            }
//
//            @Override
//            public void Correct() {
//                System.out.println("修改成功");
//            }
//        });

        // 读者创建
//        Reader reader = new Reader();
//        reader.setNo("690252189");
//        reader.setPassword("690252189");
//        reader.setName("吴贝贝");
//        reader.setGender("男");
//        reader.setType("学生");
//        reader.setCollege("电气与信息工程学院");
//        DaoFactory.getReaderDao().delAccount(reader, new SqlStateListener() {
//            @Override
//            public void Error(int ErrorCode, String ErrorMessage) {
//                System.out.println(ErrorMessage);
//            }
//
//            @Override
//            public void Correct() {
//                System.out.println("注册成功");
//            }
//        });

        // 判断账号是否存在
//        Reader reader = new Reader();
//        reader.setNo("6902189");
//        reader.setPassword("a690252189");
//        try {
//            System.out.println(DaoFactory.getReaderDao().checkReader(reader));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        // 判断图书是否存在
//        Book b = new Book();
//        b.setISBN("9787040406641");
//        try {
//            System.out.println(DaoFactory.getBookDao().checkBookOfISBN(b));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        // 借书
//        Book b = new Book();
//        b.setISBN("9787040406641");
//        Reader reader = new Reader();
//        reader.setNo("6902189");
//        reader.setPassword("a690252189");
//        DaoFactory.getReaderDao().borrowBook(reader, b, new SqlStateListener() {
//            @Override
//            public void Error(int ErrorCode, String ErrorMessage) {
//                System.out.println(ErrorMessage);
//            }
//
//            @Override
//            public void Correct() {
//                System.out.println("借书成功");
//            }
//        });

        // 还书
//        Book b = new Book();
//        b.setISBN("9787040406641");
//        Reader reader = new Reader();
//        reader.setNo("6902189");
//        reader.setPassword("a690252189");
//        DaoFactory.getReaderDao().returnBook(reader, b, new SqlStateListener() {
//            @Override
//            public void Error(int ErrorCode, String ErrorMessage) {
//                System.out.println(ErrorMessage);
//            }
//
//            @Override
//            public void Correct() {
//                System.out.println("还书成功");
//            }
//        });

        // 查询书籍
        try {
            List<Book> arrayList =  DaoFactory.getBookDao().queryBookOfKeyword("PublishDate","/");
            for(Book b : arrayList){
                System.out.println(b.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
