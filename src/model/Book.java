package model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.util.Date;

public class Book implements Serializable {
    private String ISBN;
    private String Name;
    private String Author;
    private String Type;
    private String Publisher;
    private String PublishDate;
    private int Amount = 0;
    private int Available = 0;
    private String Cover = "null";

    public Book() {
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String author) {
        Author = author;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getPublisher() {
        return Publisher;
    }

    public void setPublisher(String publisher) {
        Publisher = publisher;
    }

    public String getPublishDate() {
        return PublishDate;
    }

    public void setPublishDate(String publishDate) {
        PublishDate = publishDate;
    }

    public int getAmount() {
        return Amount;
    }

    public void setAmount(int amount) {
        Amount = amount;
    }

    public int getAvailable() {
        return Available;
    }

    public void setAvailable(int available) {
        Available = available;
    }

    public String getCover() {
        return Cover;
    }

    public void setCover(String cover) {
        Cover = cover;
    }

    @Override
    public String toString() {
//        JSONObject jsonObject = (JSONObject) JSON.toJSON(this);
//        return jsonObject.toJSONString();
        return "{" +
                "\"ISBN\":\"" + ISBN + '\"' +
                ", \"Name\":\"" + Name + '\"' +
                ", \"Author\":\"" + Author + '\"' +
                ", \"Type\":\"" + Type + '\"' +
                ", \"Publisher\":\"" + Publisher + '\"' +
                ", \"PublishDate\":\"" + PublishDate + '\"' +
                ", \"Amount\":" + Amount +
                ", \"Available\":" + Available +
                ", \"Cover\":\"" + Cover + '\"' +
                '}';
    }
}
