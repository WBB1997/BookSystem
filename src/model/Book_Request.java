package model;

public class Book_Request {
    private Reader reader;
    private Book book;
    private Staff staff;
    private String time;
    private String status;

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
                ", \"Author\":\"" + book.getAuthor() + '\"' +
                ", \"Name\":\"" + book.getName() + '\"' +
                ", \"ISBN\":\"" + book.getISBN() + '\"' +
                ", \"Publisher\":\"" + book.getPublisher() + '\"' +
                ", \"PublishDate\":\"" + book.getPublishDate() + '\"' +
                ", \"Time\":\"" + time + '\"' +
                ", \"Status\":\"" + status + '\"' +
                '}';
    }
}
