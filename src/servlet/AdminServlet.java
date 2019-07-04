package servlet;

import com.alibaba.fastjson.JSONObject;
import javafx.util.Pair;
import model.Admin;
import model.Staff;
import model.Staff_DealWith_Book_History;
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

    // 注册Staff账号
    public String registerStaffAccount(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String username = req.getParameter("no");
        String password = req.getParameter("password");
        String name = req.getParameter("name");
        String gender = req.getParameter("gender");
        String phone = req.getParameter("phone");
        HttpSession session = req.getSession();
        Admin a = (Admin) session.getAttribute("Admin");
        Staff staff = new Staff(username, password, name,gender,phone);
        JSONObject jsonObject = new JSONObject();
        if (DaoFactory.getAdminDao().checkAdmin(a)){
            DaoFactory.getStaffDao().registerAccount(staff, new SqlStateListener() {
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

        } else {
            jsonObject.put("status", "error");
            jsonObject.put("content", "管理员账号密码错误");
        }
        return jsonObject.toJSONString();
    }

    // 获取历史记录
    public String getHistory(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession(true);
        Admin a = (Admin) session.getAttribute("Admin");
        int pageNow = Integer.parseInt(req.getParameter("page"));
        int pageSize = Integer.parseInt(req.getParameter("limit"));
        String keyword = req.getParameter("keyword");
        System.out.println("keyword  " + keyword);
        Pair<List<Staff_DealWith_Book_History>, Integer> list;
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

    // 删除读者账号
    public String deleteReader(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String reader_no = req.getParameter("no");
        HttpSession session = req.getSession();
        Admin admin = (Admin) session.getAttribute("Admin");
        Reader reader = new Reader();
        reader.setNo(reader_no);
        JSONObject jsonObject = new JSONObject();
        if(DaoFactory.getAdminDao().checkAdmin(admin)) {
            DaoFactory.getReaderDao().delAccount(reader, new SqlStateListener() {
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
        }else {
            jsonObject.put("status", "error");
            jsonObject.put("content", "账号密码错误");
            return jsonObject.toJSONString();
        }
    }

    // 删除Staff账号
    public String deleteStaff(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String staff_no = req.getParameter("no");
        HttpSession session = req.getSession();
        Admin admin = (Admin) session.getAttribute("Admin");
        Staff staff = new Staff();
        staff.setNo(staff_no);
        JSONObject jsonObject = new JSONObject();
        if(DaoFactory.getAdminDao().checkAdmin(admin)) {
            DaoFactory.getStaffDao().delAccount(staff, new SqlStateListener() {
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
        }else {
            jsonObject.put("status", "error");
            jsonObject.put("content", "账号密码错误");
            return jsonObject.toJSONString();
        }
    }


    // 重置读者密码
    public String resetReaderPassword(HttpServletRequest req, HttpServletResponse resp) {
        String reader_no = req.getParameter("no");
        HttpSession session = req.getSession();
        Admin admin = (Admin) session.getAttribute("Admin");
        Reader reader = new Reader();
        reader.setNo(reader_no);
        JSONObject jsonObject = new JSONObject();
        System.out.println(reader_no);
        DaoFactory.getAdminDao().defaultReaderPassword(admin,reader, new SqlStateListener() {
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

    // 重置Staff密码
    public String resetStaffPassword(HttpServletRequest req, HttpServletResponse resp) {
        String staff_no = req.getParameter("no");
        HttpSession session = req.getSession();
        Admin admin = (Admin) session.getAttribute("Admin");
        Staff staff = new Staff();
        staff.setNo(staff_no);
        JSONObject jsonObject = new JSONObject();
        DaoFactory.getAdminDao().defaultStaffPassword(admin, staff, new SqlStateListener() {
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

    // 获取读者列表
    public String getReaderList(HttpServletRequest req, HttpServletResponse resp) {
        int pageNow = Integer.parseInt(req.getParameter("page"));
        int pageSize = Integer.parseInt(req.getParameter("limit"));
        String keyword = req.getParameter("keyWord");
        Pair<List<Reader>, Integer> list;
        try {
            if (keyword == null)
                list = DaoFactory.getReaderDao().getReaderList(pageNow, pageSize);
            else
                list = DaoFactory.getReaderDao().getReaderListInKeyWord(keyword, pageNow, pageSize);
            return RequestModel.buildSuccess(list.getValue(), list.getKey()).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return RequestModel.buildError().toString();
        }
    }

    // 获取工作人员列表
    public String getStaffList(HttpServletRequest req, HttpServletResponse resp) {
        int pageNow = Integer.parseInt(req.getParameter("page"));
        int pageSize = Integer.parseInt(req.getParameter("limit"));
        String keyword = req.getParameter("keyWord");
        Pair<List<Staff>, Integer> list;
        try {
            if (keyword == null)
                list = DaoFactory.getStaffDao().getStaffList(pageNow, pageSize);
            else
                list = DaoFactory.getStaffDao().getStaffListInKeyWord(keyword, pageNow, pageSize);
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
