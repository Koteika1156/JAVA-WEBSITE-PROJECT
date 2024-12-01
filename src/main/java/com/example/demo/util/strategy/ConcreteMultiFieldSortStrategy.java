package com.example.demo.util.strategy;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

public class ConcreteMultiFieldSortStrategy<T> implements SortStrategy<T> {
    private final List<Function<T, Comparable>> sortKeys;
    private final boolean needToReverse;

    public ConcreteMultiFieldSortStrategy(List<Function<T, Comparable>> sortKeys, boolean needToReverse) {
        this.sortKeys = sortKeys;
        this.needToReverse = needToReverse;
    }

    @Override
    public void sort(List<T> list) {
        Comparator<T> comparator = sortKeys.stream()
                .map(key -> Comparator.comparing(key, Comparator.nullsLast(Comparator.naturalOrder())))
                .reduce(Comparator::thenComparing)
                .orElse((a, b) -> 0);

        if (needToReverse) {
            comparator = comparator.reversed();
        }

        list.sort(comparator);
    }
}

