package ru.home.post.writer;

import ru.home.post.writer.commons.*;
import ru.home.post.writer.commons.Package;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class CreatePackages implements Runnable {
    private CarrierInfo carrierInfo;
    private Integer days;

    public CreatePackages(CarrierInfo carrierInfo, Integer days) {
        this.carrierInfo = carrierInfo;
        this.days = days;
    }

    @Override
    public void run() {
        DateService dateService=new DateService(carrierInfo);
        RoutePointsService routePointsService=new RoutePointsService(carrierInfo);
        PackageService packageService=new PackageService(carrierInfo);
        carrierInfo.setDateService(dateService);
        carrierInfo.setPackageService(packageService);
        carrierInfo.setRoutePointsService(routePointsService);
        for (int dayCounter = 0; dayCounter < days; dayCounter++) {
            Integer delta = WriterSettings.getPackagesPerDay() * WriterSettings.getDelta() / 100;
            Integer minPackages = WriterSettings.getPackagesPerDay() - delta;
            Integer maxPackages = WriterSettings.getPackagesPerDay() + delta;
            Random r = new Random();
            Integer bound=(maxPackages - minPackages) + 1;
            Integer packagesQuantity = r.nextInt(bound) + minPackages;
            Date workingDate=dateService.getDate();
            System.out.println(carrierInfo.getDbname()+" packages "+workingDate+" "+packagesQuantity+" "+new Date());
            for (int i = 0; i < packagesQuantity; i++) {
                Package aPackage = new Package();
                List<RoutePoint> route=routePointsService.createRoute();
                aPackage.setAcceptanceDate(workingDate);
                aPackage.setStartPoint(route.get(0));
                aPackage.setEndPoint(route.get(route.size()-1));
                packageService.savePackage(aPackage);
                packageService.saveRoute(aPackage,route);
                packageService.setState(workingDate,aPackage,Status.ACCEPTED,route.get(0));

            }
            dateService.newDate();

        }
    }
}
