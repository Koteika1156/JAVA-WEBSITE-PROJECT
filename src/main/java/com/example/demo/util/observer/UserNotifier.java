package com.example.demo.util.observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserNotifier implements Subscriber<NotifierData> {
    private static final Logger logger = LoggerFactory.getLogger(UserNotifier.class);

    @Override
    public void update(NotifierData notifierData) {
        logger.info("User:" + notifierData.getUserId() + " уведомлен о записи!");
    }
}
