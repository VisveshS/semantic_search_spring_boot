package com.example.demo.model;

public class FormState {
    public Integer useraction;
    public Integer getUseraction() {
        return useraction;
    }

    public void setUseraction(Integer useraction) {
        this.useraction = useraction;
    }
    public String args; //for QUERYSTRING and INSERT
    public String getArgs() {
        return args;
    }
    public void setArgs(String args) {
        this.args = args;
    }
}

