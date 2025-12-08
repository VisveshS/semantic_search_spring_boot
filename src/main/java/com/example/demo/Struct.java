package com.example.demo;

public class Struct<K, V, S, R> {
    public K key;
    public V value;
    public S score;
    public R reciprocal;
    public Struct(K key, V value, S score) {
        this.key = key;
        this.value = value;
        this.score = score;
    }
    public Struct(K key, V value, S score, R reciprocal) {
        this.key = key;
        this.value = value;
        this.score = score;
        this.reciprocal = reciprocal;
    }
}
