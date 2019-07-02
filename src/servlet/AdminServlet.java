package servlet;

import com.alibaba.fastjson.JSONObject;
import javafx.util.Pair;
import model.Admin;
import model.AdminHistory;
import model.Reader;
import service.RequestModel;
import util.DaoFactory;
import util.SqlStateListener;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "AdminServlet", urlPatterns = {"/AdminServlet"})
public class AdminServlet extends BaseServlet {

    // 确认账号密码
    public String checkAccount(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String username = req.getParameter("account");
        String password = req.getParameter("password");
        HttpSession session = req.getSession(true);
        Admin a = new Admin(username, password);
        if (DaoFactory.getAdminDao().checkAdmin(a)) {
            session.setAttribute("Admin", DaoFactory.getAdminDao().getAdminDetails(a));
            resp.sendRedirect(req.getContextPath() + "/admin/control.jsp");
        } else {
            session.setAttribute("message", "用户名或密码错误，请重新输入！");
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
        }
        return null;
    }

    //注册账号密码
    public String registerAccount(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String username = req.getParameter("account");
        String password = req.getParameter("password");
        HttpSession session = req.getSession(true);
        Admin a = new Admin(username, password);
        if (DaoFactory.getAdminDao().checkAdmin(a)) {
            session.setAttribute("Admin", DaoFactory.getAdminDao().getAdminDetails(a));
            resp.sendRedirect(req.getContextPath() + "/admin/control.jsp");
        } else {
            session.setAttribute("message", "用户名或密码错误，请重新输入！");
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
        }
        return null;
    }

    // 获取历史记录
    public String getHistory(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession(true);
        Admin a = (Admin) session.getAttribute("Admin");
        int pageNow = Integer.parseInt(req.getParameter("page"));
        int pageSize = Integer.parseInt(req.getParameter("limit"));
        String keyword = req.getParameter("keyword");
        System.out.println("keyword  " + keyword);
        Pair<List<AdminHistory>, Integer> list;
        try {
            if (keyword == null)
                list = DaoFactory.getAdminDao().queryHistory(a, pageNow, pageSize);
            else
                list = DaoFactory.getAdminDao().queryHistoryInWord(a, keyword, pageNow, pageSize);
            return RequestModel.buildSuccess(list.getValue(), list.getKey()).toString();
        } catch (Exception e) {
            return RequestModel.buildError().toString();
        }
    }

    // 更新管理员
    public String updateAdmin(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String no = req.getParameter("no");
        String old_password = req.getParameter("old_password");
        String new_password = req.getParameter("new_password");
        String name = req.getParameter("name");
        String gender = req.getParameter("gender");
        String phone = req.getParameter("phone");
        Admin a = new Admin(no, old_password);
        JSONObject jsonObject = new JSONObject();
        if (DaoFactory.getAdminDao().checkAdmin(a)) {
            a.setPassword(new_password);
            a.setName(name);
            a.setGender(gender);
            a.setPhone(phone);
            System.out.println(a.toString());
            DaoFactory.getAdminDao().modAccount(a, new SqlStateListener() {
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
                    session.setAttribute("Admin", a);
                }
            });
        } else {
            jsonObject.put("status", "error");
            jsonObject.put("content", "旧密码错误");
        }
        return jsonObject.toJSONString();
    }

    // 获取读者列表
    public String getReaderList(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession(true);
        Admin a = (Admin) session.getAttribute("Admin");
        int pageNow = Integer.parseInt(req.getParameter("page"));
        int pageSize = Integer.parseInt(req.getParameter("limit"));
        try {
            Pair<List<Reader>, Integer> list = DaoFactory.getAdminDao().getReaderList(a, pageNow, pageSize);
            return RequestModel.buildSuccess(list.getValue(), list.getKey()).toString();
        } catch (Exception e) {
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
