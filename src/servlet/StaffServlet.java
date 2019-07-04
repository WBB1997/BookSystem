package servlet;

import model.Staff;
import util.DaoFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
}
