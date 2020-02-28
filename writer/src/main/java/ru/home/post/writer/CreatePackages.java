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
        for (int dayCounter = 0; dayCounter < days; dayCounter++) {
            Integer delta = WriterSettings.getPackagesPerDay() * WriterSettings.getDelta() * 100;
            Integer minPackages = WriterSettings.getPackagesPerDay() - delta;
            Integer maxPackages = WriterSettings.getPackagesPerDay() + delta;
            Random r = new Random();
            Integer packagesQuantity = r.nextInt((maxPackages - minPackages) + 1) + minPackages;
            Date workingDate=dateService.getDate();
            for (int i = 0; i < packagesQuantity; i++) {
                Package aPackage = new Package();
                List<RoutePoint> route=routePointsService.createRoute();
                aPackage.setAcceptanceDate(workingDate);
                aPackage.setStartPoint(route.get(0));
                aPackage.setEndPoint(route.get(route.size()-1));
                packageService.savePackage(aPackage);
                packageService.saveRoute(aPackage,route);
                System.out.println(carrierInfo.getUser()+" "+workingDate+" "+aPackage.getId());
            }
            dateService.newDate();

        }
    }
}
