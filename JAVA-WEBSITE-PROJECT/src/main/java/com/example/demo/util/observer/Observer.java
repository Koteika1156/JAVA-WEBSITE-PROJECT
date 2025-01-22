package com.example.demo.util.observer;

public interface Observer<T> {
    void subscribe(Subscriber<T> observer);

    void unsubscribe(Subscriber<T> observer);

    void notifySubscribers(NotifierData notifierData);
}
