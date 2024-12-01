package com.example.demo.util.strategy;

import java.util.List;

// Паттерн стратегия, поведенческий, какую сортировку положишь ту он и заюзает
@FunctionalInterface
public interface SortStrategy<T> {
    void sort(List<T> list);
}
