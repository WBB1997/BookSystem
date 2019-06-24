package model;

import java.util.Date;

public class Book {
    private String No;
    private String Name;
    private String Gener;
    private String Type;
    private String College;
    private Date date;

    public String getNo() {
        return No;
    }

    public void setNo(String no) {
        No = no;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getGener() {
        return Gener;
    }

    public void setGener(String gener) {
        Gener = gener;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getCollege() {
        return College;
    }

    public void setCollege(String college) {
        College = college;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Book{" +
                "No='" + No + '\'' +
                ", Name='" + Name + '\'' +
                ", Gener='" + Gener + '\'' +
                ", Type='" + Type + '\'' +
                ", College='" + College + '\'' +
                ", date=" + date +
                '}';
    }
}
