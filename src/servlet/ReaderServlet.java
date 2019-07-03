package servlet;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import javafx.util.Pair;
import model.*;
import service.RequestModel;
import util.DaoFactory;
import util.SqlStateListener;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet( name = "ReaderServlet" , urlPatterns = {"/ReaderServlet"})
public class ReaderServlet extends BaseServlet{

    // 验证账号
    public String checkAccount(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String username = req.getParameter("account");
        String password = req.getParameter("password");
        HttpSession session = req.getSession(true);
        Reader r = new Reader(username,password);
        if (DaoFactory.getReaderDao().checkReader(r)) {
            Reader reader = DaoFactory.getReaderDao().getReaderDetails(r);
            reader.setPassword(password);
            session.setAttribute("Reader", reader);
            resp.sendRedirect(req.getContextPath() + "/reader/control.jsp");
        } else {
            session.setAttribute("message", "用户名或密码错误，请重新输入！");
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
        }
        return null;
    }

    // 注册账号
    public String registerAccount(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String username = req.getParameter("no");
        String password = req.getParameter("password");
        String name = req.getParameter("name");
        String gender = req.getParameter("gender");
        String college = req.getParameter("college");
        Reader r = new Reader(username,password);
        r.setName(name);
        r.setGender(gender);
        r.setCollege(college);
        System.out.println(r);
        JSONObject jsonObject = new JSONObject();
        DaoFactory.getReaderDao().registerAccount(r, new SqlStateListener() {
            @Override
            public void Error(int ErrorCode, String ErrorMessage) {
                jsonObject.put("status", "error");
                jsonObject.put("content", ErrorMessage);
            }

            @Override
            public void Correct() {
                jsonObject.put("status", "ok");
                jsonObject.put("content", "注册成功");
            }
        });
        System.out.println(jsonObject.toJSONString());
        return jsonObject.toJSONString();
    }


    // 获取历史纪录
    public String getHistory(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession(false);
        Reader r = (Reader) session.getAttribute("Reader");
        int pageNow = Integer.parseInt(req.getParameter("page"));
        int pageSize = Integer.parseInt(req.getParameter("limit"));
        String keyword = req.getParameter("keyword");
        Pair<List<Reader_Borrow_Return_History>, Integer> list;
        try {
            if (keyword == null)
                list = DaoFactory.getReaderDao().queryHistory(r, pageNow, pageSize);
            else
                list = DaoFactory.getReaderDao().queryHistoryInWord(r, keyword, pageNow, pageSize);
            return RequestModel.buildSuccess(list.getValue(), list.getKey()).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return RequestModel.buildError().toString();
        }
    }

    // 更新读者
    public String updateReader(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String no = req.getParameter("no");
        String old_password = req.getParameter("old_password");
        String new_password = req.getParameter("new_password");
        String name = req.getParameter("name");
        String gender = req.getParameter("gender");
        String type = req.getParameter("type");
        String college = req.getParameter("college");
        Reader r = new Reader(no,old_password);
        JSONObject jsonObject = new JSONObject();
        if(DaoFactory.getReaderDao().checkReader(r)){
            r.setPassword(new_password);
            r.setName(name);
            r.setGender(gender);
            r.setType(type);
            r.setCollege(college);
            DaoFactory.getReaderDao().modAccount(r, new SqlStateListener() {
                @Override
                public void Error(int ErrorCode, String ErrorMessage) {
                    jsonObject.put("status", "error");
                    jsonObject.put("content", ErrorMessage);
                }

                @Override
                public void Correct() {
                    jsonObject.put("status", "ok");
                    jsonObject.put("content", "修改成功");
                    HttpSession session = req.getSession();
                    try {
                        session.setAttribute("Reader", DaoFactory.getReaderDao().getReaderDetails(r));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
        }else {
            jsonObject.put("status", "error");
            jsonObject.put("content", "旧密码错误");
        }
        return jsonObject.toJSONString();
    }

    // 借书
    public String borrowBook(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String isbn = req.getParameter("isbn");
        String password = req.getParameter("password");
        String account = req.getParameter("account");
        Reader r = new Reader(account, password);
        Book b = new Book();
        b.setISBN(isbn);
        JSONObject jsonObject = new JSONObject();
        DaoFactory.getReaderDao().borrowBook(r, b, new SqlStateListener() {
            @Override
            public void Error(int ErrorCode, String ErrorMessage) {
                jsonObject.put("status", "error");
                jsonObject.put("content", ErrorMessage);
            }

            @Override
            public void Correct() {
                jsonObject.put("status", "ok");
                jsonObject.put("content", "借书成功！");
            }
        });
        return jsonObject.toJSONString();
    }

    // 还书
    public String returnBook(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String isbn = req.getParameter("isbn");
        String password = req.getParameter("password");
        String account = req.getParameter("account");
        Reader r = new Reader(account, password);
        Book b = new Book();
        b.setISBN(isbn);
        JSONObject jsonObject = new JSONObject();
        DaoFactory.getReaderDao().returnBook(r, b, new SqlStateListener() {
            @Override
            public void Error(int ErrorCode, String ErrorMessage) {
                jsonObject.put("status", "error");
                jsonObject.put("content", ErrorMessage);
            }

            @Override
            public void Correct() {
                jsonObject.put("status", "ok");
                jsonObject.put("content", "还书成功！");
            }
        });
        return jsonObject.toJSONString();
    }

    // 退出纪录
    public String loginOut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        if (null != session) {
            session.invalidate();
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
        }
        return null;
    }

    // 获取读者类型
    public String getCollegeTypes(HttpServletRequest req, HttpServletResponse resp) throws SQLException {
        JSONArray jsonArray = new JSONArray();
        jsonArray.addAll(DaoFactory.getReaderDao().getCollegeTypes());
        return jsonArray.toJSONString();
    }

    // 获取读者类型
    public String getReaderTypes(HttpServletRequest req, HttpServletResponse resp) throws SQLException {
        JSONArray jsonArray = new JSONArray();
        jsonArray.addAll(DaoFactory.getReaderDao().getReaderTypes());
        return jsonArray.toJSONString();
    }
}
