package servlet;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import javafx.util.Pair;
import model.Admin;
import model.Book;
import model.Staff;
import service.RequestModel;
import util.DaoFactory;
import util.SqlStateListener;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

@WebServlet(name = "BookServlet", urlPatterns = {"/BookServlet"})
public class BookServlet extends BaseServlet {

    //获取所有图书
    public String getAllBooks(HttpServletRequest req, HttpServletResponse resp) {
        int pageNow = Integer.parseInt(req.getParameter("page"));
        int pageSize = Integer.parseInt(req.getParameter("limit"));
        try {
            Pair<List<Book>, Integer> books = DaoFactory.getBookDao().queryAllBook("", pageNow, pageSize);
            RequestModel<Book> bookModel = RequestModel.buildSuccess(books.getValue(), books.getKey());
            return bookModel.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // 更新图书信息
    public String updateBook(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String name = req.getParameter("name");
        String isbn = req.getParameter("isbn");
        String author = req.getParameter("author");
        String type = req.getParameter("type");
        String publisher = req.getParameter("publisher");
        Double value = Double.valueOf(req.getParameter("value"));
        String cover = req.getParameter("cover");
        SimpleDateFormat sdf_input = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//输入格式
        SimpleDateFormat sdf_target = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); //转化成为的目标格式
        String publishdate = sdf_target.format(sdf_input.parse(req.getParameter("publishdate")));
        Staff a = (Staff) req.getSession().getAttribute("Staff");
        Book b = new Book();
        JSONObject jsonObject = new JSONObject();
        b.setName(name);
        b.setISBN(isbn);
        b.setAuthor(author);
        b.setType(type);
        b.setPublisher(publisher);
        b.setPublishDate(publishdate);
        b.setCover(cover);
        b.setValue(value);
        System.out.println(b);
        DaoFactory.getBookDao().modBook(b, a, new SqlStateListener() {
            @Override
            public void Error(int ErrorCode, String ErrorMessage) {
                jsonObject.put("status", "error");
                jsonObject.put("content", ErrorMessage);
            }

            @Override
            public void Correct() {
                jsonObject.put("status", "ok");
                jsonObject.put("content", "修改成功");
            }
        });
        return jsonObject.toJSONString();
    }

    // 添加图书
    public String addBook(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String name = req.getParameter("name");
        String isbn = req.getParameter("isbn");
        String author = req.getParameter("author");
        String type = req.getParameter("type");
        String publisher = req.getParameter("publisher");
        SimpleDateFormat sdf_input = new SimpleDateFormat("yyyy-MM-dd");//输入格式
        SimpleDateFormat sdf_target = new SimpleDateFormat("yyyy-MM"); //转化成为的目标格式
        String publishdate = sdf_target.format(sdf_input.parse(req.getParameter("publishdate")));
        Staff a = (Staff) req.getSession().getAttribute("Staff");
        Book b = new Book();
        JSONObject jsonObject = new JSONObject();
        b.setName(name);
        b.setISBN(isbn);
        b.setAuthor(author);
        b.setType(type);
        b.setPublisher(publisher);
        b.setPublishDate(publishdate);
        System.out.println(b);
        DaoFactory.getBookDao().addBook(a, b, new SqlStateListener() {
            @Override
            public void Error(int ErrorCode, String ErrorMessage) {
                jsonObject.put("status", "error");
                jsonObject.put("content", ErrorMessage);
            }

            @Override
            public void Correct() {
                jsonObject.put("status", "ok");
                jsonObject.put("content", "添加成功");
            }
        });
        return jsonObject.toJSONString();
    }

    // 增加图书
    public String increaseBook(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String isbn = req.getParameter("isbn");
        int quantity = Integer.parseInt(req.getParameter("quantity"));
        Staff staff = (Staff) req.getSession().getAttribute("Staff");
        Book b = new Book();
        JSONObject jsonObject = new JSONObject();
        b.setISBN(isbn);
        DaoFactory.getBookDao().increaseBook(b, quantity, staff, new SqlStateListener() {
            @Override
            public void Error(int ErrorCode, String ErrorMessage) {
                jsonObject.put("status", "error");
                jsonObject.put("content", ErrorMessage);
            }

            @Override
            public void Correct() {
                jsonObject.put("status", "ok");
                jsonObject.put("content", "增加成功");
            }
        });
        return jsonObject.toJSONString();
    }

    // 减少图书
    public String decreaseBook(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String isbn = req.getParameter("isbn");
        int quantity = Integer.parseInt(req.getParameter("quantity"));
        Staff staff = (Staff) req.getSession().getAttribute("Staff");
        Book b = new Book();
        JSONObject jsonObject = new JSONObject();
        b.setISBN(isbn);
        DaoFactory.getBookDao().decreaseBook(b, quantity, staff, new SqlStateListener() {
            @Override
            public void Error(int ErrorCode, String ErrorMessage) {
                jsonObject.put("status", "error");
                jsonObject.put("content", ErrorMessage);
            }

            @Override
            public void Correct() {
                jsonObject.put("status", "ok");
                jsonObject.put("content", "减少成功");
            }
        });
        System.out.println(jsonObject.toJSONString());
        return jsonObject.toJSONString();
    }

    public String getBookTypes(HttpServletRequest req, HttpServletResponse resp) throws SQLException {
        JSONArray jsonArray = new JSONArray();
        jsonArray.addAll(DaoFactory.getBookDao().getBookTypes());
        return jsonArray.toJSONString();
    }

    public String getBooks(HttpServletRequest req, HttpServletResponse resp) {
        int pageNow = Integer.parseInt(req.getParameter("page"));
        int pageSize = Integer.parseInt(req.getParameter("limit"));
        String keyword = req.getParameter("keyword");
        String searchword = req.getParameter("searchword");
        try {
            Pair<List<Book>, Integer> books;
            if (keyword.equals("All"))
                books = DaoFactory.getBookDao().queryAllBook(searchword, pageNow, pageSize);
            else
                books = DaoFactory.getBookDao().queryBookOfKeyword(keyword, searchword, pageNow, pageSize);
            RequestModel<Book> bookModel = RequestModel.buildSuccess(books.getValue(), books.getKey());
            return bookModel.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
