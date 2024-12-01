package com.example.demo.util.strategy;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

public class ConcreteOneFieldSortStrategy<T> implements SortStrategy<T> {
    private final boolean needToReverse;
    private final Function<T, Comparable> sortBy;

    public ConcreteOneFieldSortStrategy(Function<T, Comparable> sortBy, boolean needToReverse) {
        this.needToReverse = needToReverse;
        this.sortBy = sortBy;
    }

    @Override
    public void sort(List<T> list) {
        Comparator<T> comparator = Comparator.comparing(
                sortBy,
                Comparator.nullsLast(Comparator.naturalOrder())
        );

        if (needToReverse) {
            comparator = comparator.reversed();
        }

        list.sort(comparator);
    }
}

