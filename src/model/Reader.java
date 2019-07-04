package model;

public class Reader {
    private String No;
    private String Password;
    private String Name;
    private String Gender;
    private int Fine;
    private String TakeEffectDate;
    private String LoseEffectDate;

    public Reader() {
    }

    public int getFine() {
        return Fine;
    }

    public void setFine(int fine) {
        Fine = fine;
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
                ", \"Fine\":" + Fine +
                ", \"Name\":\"" + Name + '\"' +
                ", \"Gender\":\"" + Gender + '\"' +
                ", \"TakeEffectDate\":\"" + TakeEffectDate + '\"' +
                ", \"LoseEffectDate\":\"" + LoseEffectDate + '\"' +
                '}';
    }
}
