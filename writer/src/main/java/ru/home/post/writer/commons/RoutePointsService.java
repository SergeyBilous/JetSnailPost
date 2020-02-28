package ru.home.post.writer.commons;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class RoutePointsService {
    private CarrierInfo carrierInfo;

    public RoutePointsService(CarrierInfo carrierInfo) {
        this.carrierInfo = carrierInfo;
    }

    public RoutePoint getRoutePoint() {
        // Возвращает случайную точку
        Random r = new Random();

        Integer maxId = WriterSettings.getRoutePointsQuantity();
        Integer id = r.nextInt(maxId) + 1;
        return getRoutePoint(Long.valueOf(id));
    }
    public RoutePoint getRoutePoint(Long id) {
        String query = "select * from " + carrierInfo.getUser() + ".DELIVERY_POINTS where id=" + String.valueOf(id);
        try (Statement st = carrierInfo.getConnection().createStatement();
             ResultSet rs = st.executeQuery(query)) {
            if (!rs.next()) throw new SQLException("Route point " + id + " not found");
            RoutePoint routePoint = new RoutePoint();
            routePoint.setId(id);
            routePoint.setName(rs.getString(2));
            return routePoint;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<RoutePoint> createRoute() {
        Random r = new Random();
        List<Long> points = new ArrayList<>();
        List<RoutePoint> route=new ArrayList<>();
        Integer numOfPoints = r.nextInt((WriterSettings.getMaximumRouteLength() -
                WriterSettings.getMinimumRouteLength()) + 1) + WriterSettings.getMinimumRouteLength();
        while (points.size() <= numOfPoints) {
            Long pointId = Long.valueOf(r.nextInt((WriterSettings.getRoutePointsQuantity())) + 1);
            if (points.contains(pointId))
                continue;
            points.add(pointId);
            route.add(getRoutePoint(pointId));
        }

        return  route;
    }

}
