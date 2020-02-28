package ru.home.post.writer;

import ru.home.post.writer.commons.*;
import ru.home.post.writer.commons.Package;

import java.util.List;

public class CreateMovies implements Runnable{
private CarrierInfo carrierInfo;
private Integer days;

    public CreateMovies(CarrierInfo carrierInfo, Integer days) {
        this.carrierInfo = carrierInfo;
        this.days = days;
    }

    @Override
    public void run() {
        DateService dateService=new DateService(carrierInfo);
        while (dateService.getIteration()==0) {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        PackageService packageService=new PackageService(carrierInfo);
        RoutePointsService routePointsService = new RoutePointsService(carrierInfo);
        carrierInfo.setRoutePointsService(routePointsService);
        carrierInfo.setPackageService(packageService);
        carrierInfo.setDateService(dateService);
// Отправляем посылки, находящиеся в статусе Accepted
        List<Package> accepted=packageService.packagesInStatus(Status.ACCEPTED);
    }
}
