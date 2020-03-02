package ru.home.post.writer.entities;

public class PlannedRoutePoint implements Comparable<PlannedRoutePoint>{
    private Integer pointNumber;
    private Integer routePointRef;

    @Override
    public int compareTo(PlannedRoutePoint o) {

        return   pointNumber.compareTo(o.pointNumber);

    }
}
