package ru.home.post.writer.entties;

public class PlannedRoutePoint implements Comparable{
    private Integer pointNumber;
    private Integer routePointRef;

    @Override
    public int compareTo(Object o) {
        PlannedRoutePoint p=(PlannedRoutePoint)o;
        int i = pointNumber.compareTo(p.pointNumber);
        return i;
    }
}
