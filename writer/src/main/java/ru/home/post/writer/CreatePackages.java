package ru.home.post.writer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import ru.home.post.writer.config.Commons;
import ru.home.post.writer.entities.*;
import ru.home.post.writer.repositories.DeliveryPointRepository;
import ru.home.post.writer.repositories.PackageRepository;
import ru.home.post.writer.repositories.TimeSettingsRepository;

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
    @Autowired
    private TimeSettingsRepository timeSettingsRepository;
    @Autowired
    Environment environment;

private String sPackagesPerDay;
    public CreatePackages(Integer days) {
        this.days = days;
    }

    @Override
    public void run() {

        sPackagesPerDay=environment.getProperty("newpackages.per.day");
        Optional<TimeSettings> timeSettings = timeSettingsRepository.findById(1L);
        timeSettings.get().setDate(new Date());
        timeSettings.get().setIterationNumber(0L);
        timeSettingsRepository.save(timeSettings.get());
        for (int iterationNumber = 0; iterationNumber < days; iterationNumber++) {
            Parcel parcel = new Parcel();
            parcel.setAcceptanceDate(new Date());
            generatePlan(parcel);
            List<DeliveryStatus> startStatus = new ArrayList<>();
            DeliveryStatus status0 = new DeliveryStatus();
            status0.setOperationDate(new Date());
            status0.setStatus(new Statuses());
            status0.getStatus().setId(ParcelStatus.ACCEPTED.label);
            status0.setDeliveryPoint(parcel.getStartPoint());
            startStatus.add(status0);
            parcel.setDeliveryStatus(startStatus);
            packageRepository.save(parcel);
        }
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
        p0.setParcel(parcel);
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
                p.setParcel(parcel);
                routePlan.add(p);
                break;
            }
        }
        routePlan.add(p1);
        p1.setPointNumber(routePlan.size());
        p1.setParcel(parcel);
        parcel.setRoutePlan(routePlan);
    }

}
