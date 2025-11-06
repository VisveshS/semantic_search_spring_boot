package com.example.demo;

public class User {
    public String name;

    // Default constructor (required for Spring form binding)
    public User() {
    }

    public User(String name) {
        this.name = name;
    }

    // Getter and Setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
