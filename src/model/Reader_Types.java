package model;

import java.util.List;

public class Reader_Types {
    private List<String> types;

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    @Override
    public String toString() {
        return "Reader_Types{" +
                "types=" + types +
                '}';
    }
}
