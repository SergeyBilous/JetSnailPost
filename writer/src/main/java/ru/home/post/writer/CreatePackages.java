package ru.home.post.writer;

import org.springframework.beans.factory.annotation.Autowired;
import ru.home.post.writer.config.Commons;
import ru.home.post.writer.entities.DeliveryPoint;
import ru.home.post.writer.entities.Parcel;
import ru.home.post.writer.repositories.DeliveryPointRepository;
import ru.home.post.writer.repositories.PackageRepository;

import java.util.Date;
import java.util.Optional;

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
        generateEndPoints(parcel);
        packageRepository.save(parcel);
    }

    private void generateEndPoints(Parcel parcel) {
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
    }
}
