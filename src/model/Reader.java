package model;

import java.util.Date;

public class Reader {
    private String No;
    private String Password;
    private String Name;
    private String Gender;
    private String Type;
    private String College;
    private String TakeEffectDate;
    private String LoseEffectDate;

    public Reader() {
    }

    public Reader(String no, String password) {
        No = no;
        Password = password;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

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

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
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

    public String getTakeEffectDate() {
        return TakeEffectDate;
    }

    public void setTakeEffectDate(String takeEffectDate) {
        TakeEffectDate = takeEffectDate;
    }

    public String getLoseEffectDate() {
        return LoseEffectDate;
    }

    public void setLoseEffectDate(String loseEffectDate) {
        LoseEffectDate = loseEffectDate;
    }

    @Override
    public String toString() {
        return "{" +
                "\"No\":\"" + No + '\"' +
                ", \"Password\":\"" + Password + '\"' +
                ", \"Name\":\"" + Name + '\"' +
                ", \"Gender\":\"" + Gender + '\"' +
                ", \"Type\":\"" + Type + '\"' +
                ", \"College\":\"" + College + '\"' +
                ", \"TakeEffectDate\":\"" + TakeEffectDate + '\"' +
                ", \"LoseEffectDate\":\"" + LoseEffectDate + '\"' +
                '}';
    }
}
