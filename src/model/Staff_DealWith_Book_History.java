package model;

public class Staff_DealWith_Book_History {
    private Staff staff;
    private Book book;
    private String PurchaseDate;
    private int PurchaseAmount;

    public Staff_DealWith_Book_History() {
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
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
                "\"Cover\":\"" + book.getCover() + '\"' +
                ", \"Name\":\"" + book.getName() + '\"' +
                ", \"No\":\"" + staff.getNo() + '\"' +
                ", \"ISBN\":\"" + book.getISBN() + '\"' +
                ", \"PurchaseDate\":\"" + PurchaseDate + '\"' +
                ", \"PurchaseAmount\":" + PurchaseAmount +
                '}';
    }
}
