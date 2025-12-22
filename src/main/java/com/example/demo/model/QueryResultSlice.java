package com.example.demo.model;
import java.util.ArrayList;

public class QueryResultSlice {
    private ArrayList<String> values;

    public QueryResultSlice(ArrayList<String> values) {
        this.values = values;
    }

    public QueryResultSlice() {}

    public ArrayList<String> fetchSlice() {
        return values;
    }

    // Getter
    public ArrayList<String> getValues() {
        return values;
    }

    // Setter (optional, if you want to modify it from outside)
    public void setValues(ArrayList<String> values) {
        this.values = values;
    }
}