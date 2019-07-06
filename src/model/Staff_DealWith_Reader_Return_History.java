package model;

public class Staff_DealWith_Reader_Return_History {
    private Staff staff;
    private Reader reader;
    private Book book;
    private String time;
    private String status;

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

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
                "\"Reader_No\":\"" + reader.getNo() + "\"" +
                ", \"Cover\":\"" + book.getCover() + "\"" +
                ", \"Name\":\"" + book.getName() + "\"" +
                ", \"Time\":\"" + time + "\"" +
                ", \"Status\":\"" + status +  "\"" +
                ", \"ISBN\":\"" + book.getISBN() +  "\"" +
                ", \"Staff_No\":\"" + staff.getNo() +  "\"" +
                '}';
    }
}
