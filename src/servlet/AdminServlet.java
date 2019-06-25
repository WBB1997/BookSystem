package servlet;

import dao.impl.AdminDaoImpl;
import model.Admin;
import model.AdminHistory;
import service.RequestModel;
import util.DaoFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@WebServlet( name = "AdminServlet" , urlPatterns = {"/AdminServlet"})
public class AdminServlet extends BaseServlet {

    public String checkAccount(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String username = req.getParameter("account");
        String password = req.getParameter("password");
        HttpSession session = req.getSession(true);
        Admin a = new Admin();
        a.setNo(username);
        a.setPassword(password);
        if (DaoFactory.getAdminDao().checkAdmin(a)) {
            session.setAttribute("Admin", a);
            resp.sendRedirect(req.getContextPath() + "/admin/control.jsp");
        } else {
            session.setAttribute("message", "用户名或密码错误，请重新输入！");
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
        }
        return null;
    }

    public String getHistory(HttpServletRequest req, HttpServletResponse resp){
        HttpSession session = req.getSession(true);
        Admin a = (Admin) session.getAttribute("Admin");
        int pageNow = Integer.parseInt(req.getParameter("page"));
        int pageSize = Integer.parseInt(req.getParameter("limit"));
        try {
            int count = DaoFactory.getAdminDao().countHistory();
            List<AdminHistory> list = DaoFactory.getAdminDao().queryHistory(a, pageNow, pageSize);
            return  RequestModel.buildSuccess(count, list).toString();
        } catch (Exception e) {
            return RequestModel.buildError().toString();
        }
    }
}
