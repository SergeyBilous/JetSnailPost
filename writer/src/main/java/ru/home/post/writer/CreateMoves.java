package ru.home.post.writer;

import ru.home.post.writer.commons.*;
import ru.home.post.writer.commons.Package;

import java.util.Date;
import java.util.List;

public class CreateMoves implements Runnable {
    private CarrierInfo carrierInfo;
    private Integer days;

    public CreateMoves(CarrierInfo carrierInfo, Integer days) {
        this.carrierInfo = carrierInfo;
        this.days = days;
    }

    @Override
    public void run() {
        DateService dateService = carrierInfo.getDateService();
        while (dateService.getIteration() == 0) {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        PackageService packageService = carrierInfo.getPackageService();
        RoutePointsService routePointsService = carrierInfo.getRoutePointsService();

        for (int dayNo = 1; dayNo < days; dayNo++) {
// Отправляем посылки, находящиеся в статусе Accepted - находим следующую точку и ставим статус En route
            Date curDate=dateService.getDate(dayNo);
            List<Package> accepted = packageService.packagesInStatus(Status.ACCEPTED, curDate);
        }
    }
}
