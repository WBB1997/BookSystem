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
import java.text.SimpleDateFormat;
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
        Reader r = new Reader(username,password);
        r.setName(name);
        r.setGender(gender);
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
        Reader r = (Reader) req.getSession().getAttribute("Reader");
        Book b = new Book();
        b.setISBN(isbn);
        JSONObject jsonObject = new JSONObject();
        if(DaoFactory.getReaderDao().checkReader(r)) {
            DaoFactory.getReaderDao().borrowBook(r, b, new SqlStateListener() {
                @Override
                public void Error(int ErrorCode, String ErrorMessage) {
                    jsonObject.put("status", "error");
                    jsonObject.put("content", ErrorMessage);
                }

                @Override
                public void Correct() {
                    jsonObject.put("status", "ok");
                    jsonObject.put("content", "已提交申请，管理人员会及时审核并通知您！");
                }
            });
        }else {
            jsonObject.put("status", "error");
            jsonObject.put("content", "账号或密码错误");
        }
        return jsonObject.toJSONString();
    }

    // 还书
    public String returnBook(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String isbn = req.getParameter("isbn");
        Reader r = (Reader) req.getSession().getAttribute("Reader");
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
                jsonObject.put("content", "已提交申请，管理人员会及时审核并通知您！");
            }
        });
        return jsonObject.toJSONString();
    }

    // 取消借阅
    public String cancelBorrowApply(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String isbn = req.getParameter("isbn");
        Reader r = (Reader) req.getSession().getAttribute("Reader");
        Book b = new Book();
        b.setISBN(isbn);
        JSONObject jsonObject = new JSONObject();
        if(DaoFactory.getReaderDao().checkReader(r)) {
            DaoFactory.getReaderDao().cancelBorrowApply(r, b, new SqlStateListener() {
                @Override
                public void Error(int ErrorCode, String ErrorMessage) {
                    jsonObject.put("status", "error");
                    jsonObject.put("content", ErrorMessage);
                }

                @Override
                public void Correct() {
                    jsonObject.put("status", "ok");
                    jsonObject.put("content", "您的申请已取消！");
                }
            });
        }else {
            jsonObject.put("status", "error");
            jsonObject.put("content", "账号密码错误");
        }
        return jsonObject.toJSONString();
    }

    // 取消还书
    public String cancelReturnApply(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String isbn = req.getParameter("isbn");
        Reader r = (Reader) req.getSession().getAttribute("Reader");
        Book b = new Book();
        b.setISBN(isbn);
        JSONObject jsonObject = new JSONObject();
        if(DaoFactory.getReaderDao().checkReader(r)) {
            DaoFactory.getReaderDao().cancelReturnApply(r, b, new SqlStateListener() {
                @Override
                public void Error(int ErrorCode, String ErrorMessage) {
                    jsonObject.put("status", "error");
                    jsonObject.put("content", ErrorMessage);
                }

                @Override
                public void Correct() {
                    jsonObject.put("status", "ok");
                    jsonObject.put("content", "您的申请已取消！");
                }
            });
        }else {
            jsonObject.put("status", "error");
            jsonObject.put("content", "账号密码错误");
        }
        return jsonObject.toJSONString();
    }

    // 查询读者借阅申请记录
    public String getReaderBorrowApplyHistory(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession(false);
        Reader r = (Reader) session.getAttribute("Reader");
        int pageNow = Integer.parseInt(req.getParameter("page"));
        int pageSize = Integer.parseInt(req.getParameter("limit"));
        String keyword = req.getParameter("keyword");
        Pair<List<Staff_DealWith_Reader_Borrow_History>, Integer> list;
        try {
            if (keyword == null)
                list = DaoFactory.getReaderDao().queryStaffDealWithReaderBorrowHistory(r, pageNow, pageSize);
            else
                list = DaoFactory.getReaderDao().queryStaffDealWithReaderBorrowHistoryInWord(r, keyword, pageNow, pageSize);
            return RequestModel.buildSuccess(list.getValue(), list.getKey()).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return RequestModel.buildError().toString();
        }
    }

    // 查询读者还书申请记录
    public String getReaderReturnApplyHistory(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession(false);
        Reader r = (Reader) session.getAttribute("Reader");
        int pageNow = Integer.parseInt(req.getParameter("page"));
        int pageSize = Integer.parseInt(req.getParameter("limit"));
        String keyword = req.getParameter("keyword");
        Pair<List<Staff_DealWith_Reader_Return_History>, Integer> list;
        try {
            if (keyword == null)
                list = DaoFactory.getReaderDao().queryStaffDealWithReaderReturnHistory(r, pageNow, pageSize);
            else
                list = DaoFactory.getReaderDao().queryStaffDealWithReaderReturnHistoryInWord(r, keyword, pageNow, pageSize);
            return RequestModel.buildSuccess(list.getValue(), list.getKey()).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return RequestModel.buildError().toString();
        }
    }

    // 查询用户消息
    public String getReaderMessage(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession(false);
        Reader r = (Reader) session.getAttribute("Reader");
        int pageNow = Integer.parseInt(req.getParameter("page"));
        int pageSize = Integer.parseInt(req.getParameter("limit"));
        Pair<List<Reader_Message>, Integer> list;
        try {
            if (DaoFactory.getReaderDao().checkReader(r)) {
                list = DaoFactory.getReaderDao().getMessageList(r, pageNow, pageSize);
                return RequestModel.buildSuccess(list.getValue(), list.getKey()).toString();
            } else
                throw new Exception();
        } catch (Exception e) {
            e.printStackTrace();
            return RequestModel.buildError().toString();
        }
    }

    // 设置消息已读
    public String setMessage(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        Reader r = (Reader) req.getSession().getAttribute("Reader");
        SimpleDateFormat sdf_input = new SimpleDateFormat("yyyy-MM-dd");//输入格式
        SimpleDateFormat sdf_target = new SimpleDateFormat("yyyy-MM"); //转化成为的目标格式
        String time = sdf_target.format(sdf_input.parse(req.getParameter("time")));
        JSONObject jsonObject = new JSONObject();
        if (DaoFactory.getReaderDao().checkReader(r)) {

            DaoFactory.getReaderDao().setMessage(r, time, new SqlStateListener() {
                @Override
                public void Error(int ErrorCode, String ErrorMessage) {
                    jsonObject.put("status", "error");
                    jsonObject.put("content", ErrorMessage);
                }

                @Override
                public void Correct() {
                    jsonObject.put("status", "ok");
                    jsonObject.put("content", "消息已读！");
                }
            });
        }else{
            jsonObject.put("status", "error");
            jsonObject.put("content", "账号密码错误");
        }
        return jsonObject.toJSONString();
    }

    // 读者荐购
    public String readerBookRequest(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String isbn = req.getParameter("isbn");
        String name = req.getParameter("name");
        String author = req.getParameter("author");
        String publisher = req.getParameter("publisher");
        String publishDate = req.getParameter("publishdate");
        Reader r = (Reader) req.getSession().getAttribute("Reader");
        Book b = new Book();
        b.setISBN(isbn);
        b.setName(name);
        b.setAuthor(author);
        b.setPublisher(publisher);
        b.setPublishDate(publishDate);
        System.out.println(b);
        JSONObject jsonObject = new JSONObject();
        if(DaoFactory.getReaderDao().checkReader(r)) {
            DaoFactory.getReaderDao().bookRequest(b, r, new SqlStateListener() {
                @Override
                public void Error(int ErrorCode, String ErrorMessage) {
                    jsonObject.put("status", "error");
                    jsonObject.put("content", ErrorMessage);
                }

                @Override
                public void Correct() {
                    jsonObject.put("status", "ok");
                    jsonObject.put("content", "您的荐购已经提交！");
                }
            });
        }else {
            jsonObject.put("status", "error");
            jsonObject.put("content", "账号密码错误");
        }
        return jsonObject.toJSONString();
    }

    // 查询荐购历史
    public String getReaderRequestHistory(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession(false);
        Reader r = (Reader) session.getAttribute("Reader");
        int pageNow = Integer.parseInt(req.getParameter("page"));
        int pageSize = Integer.parseInt(req.getParameter("limit"));
        String keyword = req.getParameter("keyword");
        Pair<List<Book_Request>, Integer> list;
        try {
            if (keyword == null)
                list = DaoFactory.getReaderDao().queryBookRequest(r, pageNow, pageSize);
            else
                list = DaoFactory.getReaderDao().queryBookRequestInWord(r, keyword, pageNow, pageSize);
            return RequestModel.buildSuccess(list.getValue(), list.getKey()).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return RequestModel.buildError().toString();
        }
    }

    // 取消荐购申请
    public String cancelBookRequest(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String isbn = req.getParameter("isbn");
        Reader r = (Reader) req.getSession().getAttribute("Reader");
        Book b = new Book();
        b.setISBN(isbn);
        JSONObject jsonObject = new JSONObject();
        if(DaoFactory.getReaderDao().checkReader(r)) {
            DaoFactory.getReaderDao().cancelBookRequest(r, b, new SqlStateListener() {
                @Override
                public void Error(int ErrorCode, String ErrorMessage) {
                    jsonObject.put("status", "error");
                    jsonObject.put("content", ErrorMessage);
                }

                @Override
                public void Correct() {
                    jsonObject.put("status", "ok");
                    jsonObject.put("content", "您的申请已取消！");
                }
            });
        }else {
            jsonObject.put("status", "error");
            jsonObject.put("content", "账号密码错误");
        }
        return jsonObject.toJSONString();
    }

    // 退出
    public String loginOut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        if (null != session) {
            session.invalidate();
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
        }
        return null;
    }
}
