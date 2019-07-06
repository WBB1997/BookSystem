package servlet;

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
import java.util.List;

@WebServlet( name = "StaffServlet" , urlPatterns = {"/StaffServlet"})
public class StaffServlet extends BaseServlet{
    // 确认账号密码
    public String checkAccount(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String username = req.getParameter("account");
        String password = req.getParameter("password");
        HttpSession session = req.getSession(true);
        Staff s = new Staff(username, password);
        if (DaoFactory.getStaffDao().checkStaff(s)) {
            session.setAttribute("Staff", DaoFactory.getStaffDao().getStaffDetails(s));
            resp.sendRedirect(req.getContextPath() + "/staff/control.jsp");
        } else {
            session.setAttribute("message", "用户名或密码错误，请重新输入！");
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
        }
        return null;
    }

    // 查询读者借阅申请记录
    public String getBorrowApplyHistory(HttpServletRequest req, HttpServletResponse resp) {
        int pageNow = Integer.parseInt(req.getParameter("page"));
        int pageSize = Integer.parseInt(req.getParameter("limit"));
        String keyword = req.getParameter("keyword");
        Pair<List<Staff_DealWith_Reader_Borrow_History>, Integer> list;
        try {
            if (keyword == null)
                list = DaoFactory.getStaffDao().getStaffDealWithReaderBorrowHistory( pageNow, pageSize);
            else
                list = DaoFactory.getStaffDao().getStaffDealWithReaderBorrowHistoryWithWord(keyword, pageNow, pageSize);
            return RequestModel.buildSuccess(list.getValue(), list.getKey()).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return RequestModel.buildError().toString();
        }
    }

    // 查询读者借阅申请记录
    public String getReturnApplyHistory(HttpServletRequest req, HttpServletResponse resp) {
        int pageNow = Integer.parseInt(req.getParameter("page"));
        int pageSize = Integer.parseInt(req.getParameter("limit"));
        String keyword = req.getParameter("keyword");
        Pair<List<Staff_DealWith_Reader_Return_History>, Integer> list;
        try {
            if (keyword == null)
                list = DaoFactory.getStaffDao().getStaffDealWithReaderReturnHistory( pageNow, pageSize);
            else
                list = DaoFactory.getStaffDao().getStaffDealWithReaderReturnHistoryWithWord(keyword, pageNow, pageSize);
            return RequestModel.buildSuccess(list.getValue(), list.getKey()).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return RequestModel.buildError().toString();
        }
    }

    // 处理借阅申请
    public String dealWithBorrowApply(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String isbn = req.getParameter("isbn");
        String reader_account = req.getParameter("account");
        String keyword = req.getParameter("keyword");
        Reader r = new Reader();
        Book b = new Book();
        Staff staff = (Staff) req.getSession().getAttribute("Staff");
        b.setISBN(isbn);
        r.setNo(reader_account);
        System.out.println(b + "  " + staff + "  " + r + keyword);
        JSONObject jsonObject = new JSONObject();
        if (DaoFactory.getStaffDao().checkStaff(staff)) {
            DaoFactory.getStaffDao().dealWithBorrowBook(keyword, b, staff, r, new SqlStateListener() {
                @Override
                public void Error(int ErrorCode, String ErrorMessage) {
                    jsonObject.put("status", "error");
                    jsonObject.put("content", ErrorMessage);
                }

                @Override
                public void Correct() {
                    jsonObject.put("status", "ok");
                    jsonObject.put("content", "审核通过！");
                }
            });
        } else {
            jsonObject.put("status", "error");
            jsonObject.put("content", "账号密码错误");
        }
        return jsonObject.toJSONString();
    }

    // 处理还书申请
    public String dealWithReturnApply(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String isbn = req.getParameter("isbn");
        String reader_account = req.getParameter("account");
        String keyword = req.getParameter("keyword");
        Reader r = new Reader();
        Book b = new Book();
        Staff staff = (Staff) req.getSession().getAttribute("Staff");
        b.setISBN(isbn);
        r.setNo(reader_account);
        JSONObject jsonObject = new JSONObject();
        if (DaoFactory.getStaffDao().checkStaff(staff)) {
            DaoFactory.getStaffDao().dealWithReturnBook(keyword, b, staff, r, new SqlStateListener() {
                @Override
                public void Error(int ErrorCode, String ErrorMessage) {
                    jsonObject.put("status", "error");
                    jsonObject.put("content", ErrorMessage);
                }

                @Override
                public void Correct() {
                    jsonObject.put("status", "ok");
                    jsonObject.put("content", "审核通过！");
                }
            });
        } else {
            jsonObject.put("status", "error");
            jsonObject.put("content", "账号密码错误");
        }
        return jsonObject.toJSONString();
    }


    // 查询读者荐书申请记录
    public String getBookRequestHistory(HttpServletRequest req, HttpServletResponse resp) {
        int pageNow = Integer.parseInt(req.getParameter("page"));
        int pageSize = Integer.parseInt(req.getParameter("limit"));
        String keyword = req.getParameter("keyword");
        Pair<List<Book_Request>, Integer> list;
        try {
            if (keyword == null)
                list = DaoFactory.getStaffDao().getBookRequest( pageNow, pageSize);
            else
                list = DaoFactory.getStaffDao().getBookRequestWithWord(keyword, pageNow, pageSize);
            return RequestModel.buildSuccess(list.getValue(), list.getKey()).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return RequestModel.buildError().toString();
        }
    }

    // 处理荐书申请
    public String dealWithBookRequest(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String isbn = req.getParameter("isbn");
        String reader_account = req.getParameter("account");
        String keyword = req.getParameter("keyword");
        Reader r = new Reader();
        Book b = new Book();
        Staff staff = (Staff) req.getSession().getAttribute("Staff");
        b.setISBN(isbn);
        r.setNo(reader_account);
        JSONObject jsonObject = new JSONObject();
        if (DaoFactory.getStaffDao().checkStaff(staff)) {
            DaoFactory.getStaffDao().dealWithBookRequest(keyword, b, staff, r, new SqlStateListener() {
                @Override
                public void Error(int ErrorCode, String ErrorMessage) {
                    jsonObject.put("status", "error");
                    jsonObject.put("content", ErrorMessage);
                }

                @Override
                public void Correct() {
                    jsonObject.put("status", "ok");
                    jsonObject.put("content", "审核通过！");
                }
            });
        } else {
            jsonObject.put("status", "error");
            jsonObject.put("content", "账号密码错误");
        }
        return jsonObject.toJSONString();
    }

    // 获取历史记录
    public String getHistory(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession(true);
        Staff s = (Staff) session.getAttribute("Staff");
        int pageNow = Integer.parseInt(req.getParameter("page"));
        int pageSize = Integer.parseInt(req.getParameter("limit"));
        String keyword = req.getParameter("keyword");
        Pair<List<Staff_DealWith_Book_History>, Integer> list;
        try {
            if (keyword == null)
                list = DaoFactory.getStaffDao().queryHistory(s, pageNow, pageSize);
            else
                list = DaoFactory.getStaffDao().queryHistoryInWord(s, keyword, pageNow, pageSize);
            return RequestModel.buildSuccess(list.getValue(), list.getKey()).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return RequestModel.buildError().toString();
        }
    }

    public String loginOut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        if (null != session) {
            session.invalidate();
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
        }
        return null;
    }
}
