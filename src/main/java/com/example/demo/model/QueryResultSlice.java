package com.example.demo.model;
import com.example.model.Generic;
public class QueryResult<A,B,C,D> {
    public A id;
    public B url;
    public C description;
    public D in_degree;
    public Generic<A>[] page_metadata;
    public QueryResult(A a, B b, C c, D d) {
        this.id = a;
        this.url = b;
        this.description = c;
        this.in_degree = d;
    }
}

