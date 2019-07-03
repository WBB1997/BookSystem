package dao;

import javafx.util.Pair;
import model.Admin;
import model.Staff_DealWith_Book_History;
import model.Reader;
import util.SqlStateListener;

import java.sql.SQLException;
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
    Pair<List<Staff_DealWith_Book_History>, Integer> queryHistory(Admin a, int pageNow, int pageSize) throws Exception;

    // 获得有关键词历史记录
    Pair<List<Staff_DealWith_Book_History>, Integer> queryHistoryInWord(Admin a, String ISBN, int pageNow, int pageSize) throws Exception;

    // 获得读者列表(没有密码)
    Pair<List<Reader>, Integer> getReaderList(Admin a, int pageNow, int pageSize) throws SQLException;

    // 获取个人详情
    Admin getAdminDetails(Admin a) throws SQLException;

}
