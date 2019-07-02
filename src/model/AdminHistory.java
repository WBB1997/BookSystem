package model;

public class AdminHistory {
    private String Name;
    private String Cover;
    private String No;
    private String ISBN;
    private String PurchaseDate;
    private int PurchaseAmount;

    public AdminHistory() {
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getCover() {
        return Cover;
    }

    public void setCover(String cover) {
        Cover = cover;
    }

    public String getNo() {
        return No;
    }

    public void setNo(String no) {
        No = no;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getPurchaseDate() {
        return PurchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        PurchaseDate = purchaseDate;
    }

    public int getPurchaseAmount() {
        return PurchaseAmount;
    }

    public void setPurchaseAmount(int purchaseAmount) {
        PurchaseAmount = purchaseAmount;
    }

    @Override
    public String toString() {
        return "{" +
                "\"Cover\":\"" + Cover + '\"' +
                ", \"Name\":\"" + Name + '\"' +
                ", \"No\":\"" + No + '\"' +
                ", \"ISBN\":\"" + ISBN + '\"' +
                ", \"PurchaseDate\":\"" + PurchaseDate + '\"' +
                ", \"PurchaseAmount\":" + PurchaseAmount +
                '}';
    }
}
