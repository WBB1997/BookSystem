package dao;

import javafx.util.Pair;
import model.Admin;
import model.Reader;
import model.Staff;
import model.Staff_DealWith_Book_History;
import util.SqlStateListener;

import java.sql.SQLException;
import java.util.List;

public interface IStaffDao {
    // 注册账号
    void registerAccount(Staff s, SqlStateListener l);

    // 修改账号信息
    void modAccount(Staff s, SqlStateListener l);

    // 删除账号信息
    void delAccount(Staff s, SqlStateListener l);

    // 检查账号是否存在
    boolean checkStaffAccount(Staff s) throws Exception;

    // 核对账号密码
    boolean checkStaff(Staff s) throws Exception;

    // 获得历史记录
    Pair<List<Staff_DealWith_Book_History>, Integer> queryHistory(Staff s, int pageNow, int pageSize) throws Exception;

    // 获得有关键词历史记录
    Pair<List<Staff_DealWith_Book_History>, Integer> queryHistoryInWord(Staff s, String ISBN, int pageNow, int pageSize) throws Exception;

    // 获得Staff列表
    Pair<List<Staff>, Integer> getStaffList(int pageNow, int pageSize) throws SQLException;

    // 获得Staff列表
    Pair<List<Staff>, Integer> getStaffListInKeyWord(String keyword, int pageNow, int pageSize) throws SQLException;

    // 获取个人详情
    Staff getStaffDetails(Staff s) throws SQLException;
}
