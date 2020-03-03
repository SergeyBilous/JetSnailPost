package ru.home.post.writer.config;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class Commons {
    private static Integer deliveryPointsQuantity = 42;
    private static Random random;

    static {
        random = new Random();
    }

    public static Integer getDeliveryPointsQuantity() {
        return deliveryPointsQuantity;
    }
    public static int getRandom(int min,int max){
        return random.nextInt((max - min) + 1) + min;
    }
    public static Date addDays(Date date,int days){
        Calendar c=Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE,days);
        return c.getTime();
    }
}
