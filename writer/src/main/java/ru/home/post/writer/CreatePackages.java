package ru.home.post.writer;

import org.springframework.beans.factory.annotation.Autowired;
import ru.home.post.writer.config.Commons;
import ru.home.post.writer.entities.DeliveryPoint;
import ru.home.post.writer.entities.Parcel;
import ru.home.post.writer.entities.PlannedPoint;
import ru.home.post.writer.repositories.DeliveryPointRepository;
import ru.home.post.writer.repositories.PackageRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CreatePackages implements Runnable {
    private Integer days;
    @Autowired
    private PackageRepository packageRepository;
    @Autowired
    private DeliveryPointRepository deliveryPointRepository;

    public CreatePackages(Integer days) {
        this.days = days;
    }

    @Override
    public void run() {
        Parcel parcel = new Parcel();
        parcel.setAcceptanceDate(new Date());
        generatePlan(parcel);
        packageRepository.save(parcel);
    }

    private void generatePlan(Parcel parcel) {
        Long idStart = Long.valueOf(Commons.getRandom(1, Commons.getDeliveryPointsQuantity()));
        Long idEnd;
        while (true) {
            idEnd = Long.valueOf(Commons.getRandom(1, Commons.getDeliveryPointsQuantity()));
            if (idStart.equals(idEnd))
                continue;
            else
                break;
        }
        Optional<DeliveryPoint> startPoint = deliveryPointRepository.findById(idStart);
        Optional<DeliveryPoint> endPoint = deliveryPointRepository.findById(idEnd);
        parcel.setStartPoint(startPoint.get());
        parcel.setEndPoint(endPoint.get());
        List<PlannedPoint> routePlan = new ArrayList<>();
        PlannedPoint p0 = new PlannedPoint();
        p0.setDeliveryPoint(startPoint.get());
        p0.setPointNumber(1);
        PlannedPoint p1 = new PlannedPoint();
        p1.setDeliveryPoint(endPoint.get());
        routePlan.add(p0);

        int numOfPoints = Commons.getRandom(1, 10);
        for (int pointNum = 1; pointNum <= numOfPoints; pointNum++) {
            Optional<DeliveryPoint> deliveryPoint;
            while (true) {
                Long pointId = Long.valueOf(Commons.getRandom(1, 42));
                deliveryPoint = deliveryPointRepository.findById(pointId);
                Long id = deliveryPoint.get().getId();
                List<PlannedPoint> pointAddedPreviously = routePlan.stream().
                        filter(p -> p.getDeliveryPoint().getId().equals(id)).collect(Collectors.toList());
                if (pointAddedPreviously.size() > 0)
                    continue;
                PlannedPoint p = new PlannedPoint();
                p.setPointNumber(pointNum + 1);
                p.setDeliveryPoint(deliveryPoint.get());
                routePlan.add(p);
                break;
            }
        }
        routePlan.add(p1);
        p1.setPointNumber(routePlan.size());
      parcel.setRoutePlan(routePlan);
    }

}
