package com.example.demo.model;
import java.util.ArrayList;

public class QueryResultSlice {
    private ArrayList<String> values;

    public QueryResultSlice(ArrayList<String> values) {
        this.values = values;
    }

    public QueryResultSlice() {}

    public static String fetchSourceSite(String url) {
        if (url.contains("youtube")||url.contains("youtu.be"))
            return "youtube";
        else if(url.contains("instagram"))
            return "instagram";
        else if(url.contains("dropbox") || url.contains("docs.google"))
            return "my notes";
        else if(url.contains("twitter") || url.contains("x.com"))
            return "twitter";
        else if(url.contains("facebook"))
            return "facebook";
        else if(url.contains("4plebs.org") || url.contains("i.4pcdn.org"))
            return "4chan";
        else
            return "site";
    }

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