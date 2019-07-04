package model;

public class Reader_Borrow_Return_History {
    private Reader reader;
    private Book book;
    private String BorrowDate;
    private String ShouldReturnDate;
    private String ReturnDate;

    public Reader getReader() {
        return reader;
    }

    public void setReader(Reader reader) {
        this.reader = reader;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
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
                "\"No\":\"" + reader.getNo() + '\"' +
                ", \"Cover\":\"" + book.getCover() + '\"' +
                ", \"Name\":\"" + book.getName() + '\"' +
                ", \"ISBN\":\"" + book.getISBN() + '\"' +
                ", \"BorrowDate\":\"" + BorrowDate + '\"' +
                ", \"ShouldReturnDate\":\"" + ShouldReturnDate + '\"' +
                ", \"ReturnDate\":\"" + ReturnDate + '\"' +
                '}';
    }
}
