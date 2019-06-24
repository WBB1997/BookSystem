package model;

import java.util.Date;

public class Reader {
    private String No;
    private String Name;
    private String Gender;
    private String Type;
    private String College;
    private Date TakeEffectDate;
    private Date LoseEffectDate;

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

    public Date getTakeEffectDate() {
        return TakeEffectDate;
    }

    public void setTakeEffectDate(Date takeEffectDate) {
        TakeEffectDate = takeEffectDate;
    }

    public Date getLoseEffectDate() {
        return LoseEffectDate;
    }

    public void setLoseEffectDate(Date loseEffectDate) {
        LoseEffectDate = loseEffectDate;
    }

    @Override
    public String toString() {
        return "Reader{" +
                "No='" + No + '\'' +
                ", Name='" + Name + '\'' +
                ", Gender='" + Gender + '\'' +
                ", Type='" + Type + '\'' +
                ", College='" + College + '\'' +
                ", TakeEffectDate=" + TakeEffectDate +
                ", LoseEffectDate=" + LoseEffectDate +
                '}';
    }
}
