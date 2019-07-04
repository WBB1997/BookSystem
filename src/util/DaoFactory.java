package util;

import dao.IAdminDao;
import dao.IBookDao;
import dao.IReaderDao;
import dao.IStaffDao;
import dao.impl.AdminDaoImpl;
import dao.impl.BookDaoImpl;
import dao.impl.ReaderDaoImpl;
import dao.impl.StaffDaoImpl;

public class DaoFactory {
    public static IAdminDao getAdminDao(){
        return new AdminDaoImpl();
    }

    public static IBookDao getBookDao(){
        return new BookDaoImpl();
    }

    public static IReaderDao getReaderDao(){
        return new ReaderDaoImpl();
    }

    public static IStaffDao getStaffDao(){
        return new StaffDaoImpl();
    }
}
