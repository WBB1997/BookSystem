package dao;

import model.Admin;
import util.SqlStateListener;

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
}
