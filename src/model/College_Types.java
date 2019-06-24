package model;

import java.util.List;

public class College_Types {
    private List<String> types;

    public College_Types() {
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    @Override
    public String toString() {
        return "College_Types{" +
                "types=" + types +
                '}';
    }
}
