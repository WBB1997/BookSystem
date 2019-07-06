package model;

public class Reader_Message {
    private Reader reader;
    private String time;
    private String title;
    private String message;
    private String status;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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
                ", \"Time\":\"" + time + '\"' +
                ", \"Title\":\"" + title + '\"' +
                ", \"Message\":\"" + message + '\"' +
                ", \"Status\":\"" + status + '\"' +
                '}';
    }
}
