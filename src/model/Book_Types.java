package model;

import java.util.List;

public class Book_Types {
    private List<String> types;

    public Book_Types() {
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    @Override
    public String toString() {
        return "Book_Types{" +
                "types=" + types +
                '}';
    }
}
