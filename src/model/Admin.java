package model;

public class Admin {
    private String No;
    private String Password;
    private String Name;
    private String Gender;
    private String Phone;

    public Admin() {
    }

    public Admin(String no, String password) {
        No = no;
        Password = password;
    }

    public String getNo() {
        return No;
    }

    public void setNo(String no) {
        No = no;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
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

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    @Override
    public String toString() {
        return "{" +
                "\"No\":\"" + No + '\"' +
                ", \"Password\":\"" + Password + '\"' +
                ", \"Name\":\"" + Name + '\"' +
                ", \"Gender\":\"" + Gender + '\"' +
                ", \"Phone\":\"" + Phone + '\"' +
                '}';
    }
}
