package com.example.demo.util.observer;

import java.util.HashSet;

public class ScheduleNotifier implements Observer<NotifierData> {
    private final HashSet<Subscriber<NotifierData>> observers = new HashSet<>();

    @Override
    public void subscribe(Subscriber<NotifierData> observer) {
        observers.add(observer);
    }

    @Override
    public void unsubscribe(Subscriber<NotifierData> observer) {
        observers.remove(observer);
    }

    @Override
    public void notifySubscribers(NotifierData notifierData) {
        for (Subscriber<NotifierData> subscriber : observers) {
            subscriber.update(notifierData);
        }
    }
}
