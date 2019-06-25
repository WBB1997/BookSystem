package dao;

import model.Admin;
import model.AdminHistory;
import model.Book;
import util.SqlStateListener;

import java.util.List;

public interface IAdminDao {
    // 注册账号
    void registerAccount(Admin a, SqlStateListener l);
    // 修改账号信息
    void modAccount(Admin a, SqlStateListener l);
    // 删除账号信息
    void delAccount(Admin a, SqlStateListener l);
    // 检查账号是否存在
    boolean checkAdminAccount(Admin a) throws Exception;
    // 核对账号密码
    boolean checkAdmin(Admin a) throws Exception;
    // 获得历史记录
    List<AdminHistory> queryHistory(Admin a, int pageNow, int pageSize) throws Exception;
    // 获得历史记录总数
    int countHistory() throws Exception;
}
