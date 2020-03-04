package ru.home.post.writer.config;

import ru.home.post.writer.entities.DeliveryPoint;
import ru.home.post.writer.entities.DeliveryStatus;
import ru.home.post.writer.entities.Parcel;
import ru.home.post.writer.entities.ParcelStatus;

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
    public static DeliveryPoint getNextPoint(Parcel parcel){
        DeliveryPoint deliveryPoint=null;
        DeliveryStatus currentStatus=null;
        Date currentDate=null;
        for(DeliveryStatus ds : parcel.getDeliveryStatus()){
            if(currentDate==null){
                currentDate=ds.getOperationDate();
                currentStatus=ds;
            }else{
                if(currentDate.compareTo(ds.getOperationDate())>0){
                    currentDate=ds.getOperationDate();
                    currentStatus=ds;
                }
            }
        }
        DeliveryPoint currentPoint=currentStatus.getDeliveryPoint();
        if(currentStatus.getStatus().getId().equals(ParcelStatus.ACCEPTED.label)){
            deliveryPoint=currentPoint;
        }
        return deliveryPoint;
    }


}
