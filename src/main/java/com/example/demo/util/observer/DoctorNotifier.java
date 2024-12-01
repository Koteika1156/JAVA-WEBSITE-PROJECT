package com.example.demo.util.observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DoctorNotifier implements Subscriber<NotifierData> {
    private static final Logger logger = LoggerFactory.getLogger(DoctorNotifier.class);


    @Override
    public void update(NotifierData notifierData) {
        logger.info("Doctor:" + notifierData.getDoctorId() + " уведомлен о записи!");
    }
}
