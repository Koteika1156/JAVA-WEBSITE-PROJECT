package com.example.demo.models;


// Паттерн прототип, порождающий предоставляет интерфейс для создания копий
@FunctionalInterface
public interface Prototype<T> {
    T clone();
}
