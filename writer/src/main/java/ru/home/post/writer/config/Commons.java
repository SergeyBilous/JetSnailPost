package ru.home.post.writer.config;

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
}
