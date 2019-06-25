package servlet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import dao.impl.BookDaoImpl;
import model.Book;
import service.RequestModel;
import util.DaoFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@WebServlet( name = "BookServlet" , urlPatterns = {"/BookServlet"})
public class BookServlet extends BaseServlet {

    public String getAllBooks(HttpServletRequest req, HttpServletResponse resp){
        int pageNow = Integer.parseInt(req.getParameter("page"));
        int pageSize = Integer.parseInt(req.getParameter("limit"));
        try {
            int count = DaoFactory.getBookDao().countBook();
            List<Book> books = DaoFactory.getBookDao().queryAllBook("", pageNow, pageSize);
            RequestModel<Book> bookModel = RequestModel.buildSuccess(count,books);
            return bookModel.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
