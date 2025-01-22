package org.example.authservice.models;

// Паттерн прототип, порождающий предоставляет интерфейс для создания копий
@FunctionalInterface
public interface Prototype<T> {
    T clone();
}
