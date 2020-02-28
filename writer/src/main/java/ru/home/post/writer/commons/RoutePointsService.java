package ru.home.post.writer.commons;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
        List<RoutePoint> route = new ArrayList<>();
        Integer numOfPoints = r.nextInt((WriterSettings.getMaximumRouteLength() -
                WriterSettings.getMinimumRouteLength()) + 1) + WriterSettings.getMinimumRouteLength();
        while (points.size() <= numOfPoints) {
            Long pointId = Long.valueOf(r.nextInt((WriterSettings.getRoutePointsQuantity())) + 1);
            if (points.contains(pointId))
                continue;
            points.add(pointId);
            route.add(getRoutePoint(pointId));
        }

        return route;
    }

    public RoutePoint nextPoint(Package aPackage) {
        Long packageId = aPackage.getId();
        List<Long> points = new ArrayList<>();
        RoutePoint nextPoint = new RoutePoint();
        String pointsQuery = "select point_ref from " + carrierInfo.getUser() + ".DELIVERY_STATUS where PACKAGE_REF=" + String.valueOf(packageId);
        try (Statement st = carrierInfo.getConnection().createStatement();
             ResultSet rs = st.executeQuery(pointsQuery)) {
            while (rs.next()) {
                points.add(rs.getLong(1));
            }
            String nextPointQuery="select * from "+carrierInfo.getUser()+".PLANNDED_ROUTES where package_ref="+String.valueOf(packageId);
            String excludePreviousPoints=" and point_ref not in (";
            for(Long point:points){
                excludePreviousPoints+=String.valueOf(point)+",";
            }
            excludePreviousPoints=excludePreviousPoints.substring(0,excludePreviousPoints.length()-1)+")";
            excludePreviousPoints+="  order by point_number asc";
            nextPointQuery+=excludePreviousPoints;
            try(Statement nextPointsSt=carrierInfo.getConnection().createStatement();
            ResultSet nextPointRs=nextPointsSt.executeQuery(nextPointQuery)){
                nextPointRs.next();
                Long pointId=nextPointRs.getLong(3);
                nextPoint= getRoutePoint(pointId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nextPoint;
    }

}
