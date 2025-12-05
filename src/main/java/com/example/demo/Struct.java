package com.example.demo;

public class Struct<K, V, C> {
    public K key;
    public V value;
    public C color;
    public Struct(K key, V value, C color) { this.key = key; this.value = value; this.color = color; }
}
