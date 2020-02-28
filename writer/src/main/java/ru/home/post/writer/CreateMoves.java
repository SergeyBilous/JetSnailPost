package ru.home.post.writer;

import ru.home.post.writer.commons.*;
import ru.home.post.writer.commons.Package;

import java.util.*;

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
        Random r = new Random();
        for (int dayNo = 1; dayNo < days; dayNo++) {
            HashSet<Long> packagesProcessed = new HashSet<>();
            // Отправляем посылки, находящиеся в статусе Accepted - находим следующую точку и ставим статус En route
// Посылки в статусе En route - приземляем в следующей точке и ставим статус waiting for transport, если
// точка не последняя и ready for delivery - если перешли в конечную точку
// Посллки в статусе waiting for transport переводим в статус en route
            Date curDate = dateService.getDate(dayNo);
            List<Package> accepted = packageService.packagesInStatus(Status.ACCEPTED, curDate);
            List<Package> enRoute = packageService.packagesInStatus(Status.EN_ROUTE, curDate);
            List<Package> toGround = packageService.packagesInStatus(Status.WAITING_FOR_TRANSPORT, curDate);
            int counter = r.nextInt(WriterSettings.getMovesPerDay());
            for (int i = 0; i < counter; i++) {
                int index;
                Long packageId;
                while (true) {
                    index = r.nextInt(accepted.size());
                    packageId = accepted.get(index).getId();
                    if (!packagesProcessed.contains(packageId)) {
                        packagesProcessed.add(packageId);
                        break;
                    }
                }
                Package currentPackage = packageService.getPackage(packageId);
                RoutePoint nextPoint = routePointsService.nextPoint(currentPackage);
                packageService.setState(curDate, currentPackage, Status.EN_ROUTE, nextPoint);
                System.out.println("Accepted->En route :"+currentPackage.getId());
            }
        }
    }
}
