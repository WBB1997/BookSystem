package model;

public class Book_Subscribe {
    private Book book;
    private Reader reader;
    private String time;
    private String status;

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Reader getReader() {
        return reader;
    }

    public void setReader(Reader reader) {
        this.reader = reader;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "{" +
                "\"No\":\"" + reader.getNo() + '\"' +
                ", \"Name\":\"" + book.getName() + '\"' +
                ", \"ISBN\":\"" + book.getISBN() + '\"' +
                ", \"Time\":\"" + time + '\"' +
                ", \"Status\":\"" + status + '\"' +
                '}';
    }
}
