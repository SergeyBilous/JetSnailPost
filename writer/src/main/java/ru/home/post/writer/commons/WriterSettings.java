package ru.home.post.writer.commons;

public class WriterSettings {
    private static final Integer packagesPerDay = 800;
    private static final Integer delta=20; // Создание посылок и движений - _____PerDay +- delta %
    private static final Integer maximumRouteLength = 10;
    private static final Integer movesPerDay = 500;
    private static final Integer routePointsQuantity = 42;
    private static final Integer minimumRouteLength = 2;

    public static Integer getMinimumRouteLength() {
        return minimumRouteLength;
    }

    public static Integer getDelta() {
        return delta;
    }

    public WriterSettings() {
    }

    public static Integer getPackagesPerDay() {
        return packagesPerDay;
    }

    public static Integer getMaximumRouteLength() {
        return maximumRouteLength;
    }

    public static Integer getMovesPerDay() {
        return movesPerDay;
    }

    public static Integer getRoutePointsQuantity() {
        return routePointsQuantity;
    }
}
