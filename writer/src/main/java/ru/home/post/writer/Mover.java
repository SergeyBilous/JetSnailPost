package ru.home.post.writer;

import ru.home.post.writer.commons.CarrierInfo;
import ru.home.post.writer.commons.DateService;
import ru.home.post.writer.commons.PackageService;
import ru.home.post.writer.commons.RoutePointsService;

public class Mover {
    public static void main(String[] args) {
        CarrierInfo carrierInfo = new CarrierInfo("hadoop.home.ru", "logistic1", "carrier", "welcome1");
        carrierInfo.setDateService(new DateService(carrierInfo));
        carrierInfo.setPackageService(new PackageService(carrierInfo));
        carrierInfo.setRoutePointsService(new RoutePointsService(carrierInfo));

        CreateMoves movesCreator = new CreateMoves(carrierInfo, 10);
        Thread movesThread = new Thread(movesCreator);
        movesThread.start();
    }
}
