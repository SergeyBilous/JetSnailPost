package ru.home.post.writer.config;

import ru.home.post.writer.entities.*;

import java.util.*;

public class Commons {
    private static Integer deliveryPointsQuantity = 42;
    private static Random random;

    static {
        random = new Random();
    }

    public static Integer getDeliveryPointsQuantity() {
        return deliveryPointsQuantity;
    }

    public static int getRandom(int min, int max) {
        return random.nextInt((max - min) + 1) + min;
    }

    public static Date addDays(Date date, int days) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, days);
        return c.getTime();
    }

    public static DeliveryPoint getNextPoint(Parcel parcel) {
        DeliveryPoint deliveryPoint = null;
        DeliveryStatus currentStatus = null;
        Date currentDate = null;
        for (DeliveryStatus ds : parcel.getDeliveryStatus()) {
            if (currentDate == null) {
                currentDate = ds.getOperationDate();
                currentStatus = ds;
            } else {
                if (currentDate.compareTo(ds.getOperationDate()) > 0) {
                    currentDate = ds.getOperationDate();
                    currentStatus = ds;
                }
            }
        }
        DeliveryPoint currentPoint = currentStatus.getDeliveryPoint();
        if (currentStatus.getStatus().getId().equals(ParcelStatus.ACCEPTED.label) ||
                currentStatus.getStatus().getId().equals(ParcelStatus.WAITING_FOR_TRANSPORT.label)) {
            deliveryPoint = currentPoint;
        } else {
            // Ищем текущую точку в плане, по ее номеру находим следующую
            Collection<PlannedPoint> plannedPoints = parcel.getRoutePlan();
            PlannedPoint plannedPoint = null;
            for (PlannedPoint p : plannedPoints) {
                if (p.getDeliveryPoint().getId().equals(currentPoint.getId())) {
                    plannedPoint = p;
                    break;
                }
            }
            Integer pointNumber = plannedPoint.getPointNumber();
            for (PlannedPoint p : plannedPoints) {
                if (p.getPointNumber().equals(pointNumber + 1)) {
                    deliveryPoint = p.getDeliveryPoint();
                    break;
                }
            }
        }
        return deliveryPoint;
    }

    public static Iterator<Integer> randomIterator(int quantity, int min, int max) {
        ArrayList<Integer> response = new ArrayList<>();
        for (int i = 0; i < quantity; i++) {
            while (true) {
                Integer next = getRandom(min, max);
                if (!response.contains(next)) {
                    response.add(next);
                    break;
                }
            }
        }
        return response.iterator();
    }

}
