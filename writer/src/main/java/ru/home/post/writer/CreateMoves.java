package ru.home.post.writer;

import ru.home.post.writer.commons.Package;
import ru.home.post.writer.commons.*;

import java.util.*;

public class CreateMoves implements Runnable {
    private CarrierInfo carrierInfo;
    private Integer days;
    private PackageService packageService;
    private DateService dateService;
    private RoutePointsService routePointsService;
    Random random;

    public CreateMoves(CarrierInfo carrierInfo, Integer days) {
        this.carrierInfo = carrierInfo;

        this.days = days;
        dateService = carrierInfo.getDateService();
        packageService = carrierInfo.getPackageService();
        routePointsService = carrierInfo.getRoutePointsService();
        random = new Random();
    }

    @Override
    public void run() {
// Отправляем посылки, находящиеся в статусе Accepted - находим следующую точку и ставим статус En route
// Посылки в статусе En route - приземляем в следующей точке и ставим статус waiting for transport, если
// точка не последняя и ready for delivery - если перешли в конечную точку
// Посылки в статусе waiting for transport переводим в статус en route
        Random r = new Random();
        for (int dayNo = 1; dayNo < days; dayNo++) {
            HashSet<Long> packagesProcessed = new HashSet<>();
            Date curDate = dateService.getDate(dayNo);
            List<Package> accepted = packageService.packagesInStatus(Status.ACCEPTED, curDate);
            List<Package> toGround = packageService.packagesInStatus(Status.EN_ROUTE, curDate);
            List<Package> toMove = packageService.packagesInStatus(Status.WAITING_FOR_TRANSPORT, curDate);

            int counter = r.nextInt(WriterSettings.getMovesPerDay());
            for (int i = 0; i < counter; i++) {
                Package currentPackage = nextPackage(accepted, packagesProcessed);
                if(currentPackage==null)
                    break;
                createMove(curDate, currentPackage, Status.EN_ROUTE);

            }
            counter = r.nextInt(WriterSettings.getMovesPerDay());
            for (int i = 0; i < counter; i++) {
                if(toGround.size()<=0)
                    break;
                Package currentPackage = nextPackage(toGround, packagesProcessed);
                if(currentPackage==null)
                    break;
                createMove(curDate, currentPackage, Status.WAITING_FOR_TRANSPORT);

            }
            counter = r.nextInt(WriterSettings.getMovesPerDay());
            for (int i = 0; i < counter; i++) {
                if(toMove.size()<=0)
                    break;
                Package currentPackage = nextPackage(toMove, packagesProcessed);
                if(currentPackage==null)
                    break;

                createMove(curDate, currentPackage, Status.EN_ROUTE);
            }
        }
    }

    private void createMove(Date date, Package aPackage, Status toStatus) {
        System.out.println("-----");
        System.out.println(date+" "+aPackage.getId()+" "+toStatus);
        RoutePoint nextPoint = routePointsService.nextPoint(aPackage);
        if (toStatus.equals(Status.WAITING_FOR_TRANSPORT)) {
            Status finalStatus;
            if (nextPoint.equals(aPackage.getEndPoint())) {
                finalStatus = Status.READY_FOR_DELIVERY;
            } else {
                finalStatus = toStatus;
            }
            packageService.setState(date, aPackage, finalStatus, nextPoint);
        } else {
            if (toStatus.equals(Status.EN_ROUTE)) {
                RoutePoint currentPoint=packageService.getCurrentPoint(aPackage);
                packageService.setState(date, aPackage, toStatus, currentPoint);
            } else
                packageService.setState(date, aPackage, toStatus, nextPoint);
        }
    }

    private Package nextPackage(List<Package> ready, Set<Long> processed) {
       Integer attempts=0;
       Integer maxAttemts=ready.size();
        while (true) {
            int index = random.nextInt(ready.size());
            Long id = ready.get(index).getId();
            if (!processed.contains(id)) {
                processed.add(id);
                return ready.get(index);
            }else{
                attempts++;
                if(attempts>=maxAttemts)
                    return null;
            }

        }

    }
}
