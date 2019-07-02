package model;

public class ReaderHistory {
    private String No;
    private String Name;
    private String Cover;
    private String ISBN;
    private String BorrowDate;
    private String ShouldReturnDate;
    private String ReturnDate;

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

    public String getBorrowDate() {
        return BorrowDate;
    }

    public void setBorrowDate(String borrowDate) {
        BorrowDate = borrowDate;
    }

    public String getShouldReturnDate() {
        return ShouldReturnDate;
    }

    public void setShouldReturnDate(String shouldReturnDate) {
        ShouldReturnDate = shouldReturnDate;
    }

    public String getReturnDate() {
        return ReturnDate;
    }

    public void setReturnDate(String returnDate) {
        ReturnDate = returnDate;
    }

    @Override
    public String toString() {
        return "{" +
                "\"No\":\"" + No + '\"' +
                ", \"Cover\":\"" + Cover + '\"' +
                ", \"Name\":\"" + Name + '\"' +
                ", \"ISBN\":\"" + ISBN + '\"' +
                ", \"BorrowDate\":\"" + BorrowDate + '\"' +
                ", \"ShouldReturnDate\":\"" + ShouldReturnDate + '\"' +
                ", \"ReturnDate\":\"" + ReturnDate + '\"' +
                '}';
    }
}
