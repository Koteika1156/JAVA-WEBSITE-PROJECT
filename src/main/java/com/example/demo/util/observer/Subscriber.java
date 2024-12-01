package com.example.demo.util.observer;

public interface Subscriber<T> {
    void update(T t);
}
