package ru.home.post.writer.commons;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class PackageService {
    private CarrierInfo carrierInfo;

    public PackageService(CarrierInfo carrierInfo) {
        this.carrierInfo = carrierInfo;
    }

    public static Package createPackage() {
        return new Package();
    }

    public Boolean savePackage(Package p) {

        String query = "insert into " + carrierInfo.getUser() + ".packages(acceptance_date,start_point,end_point) values(?,?,?)";
        String[] generatedCols = {"ID"};
        try (PreparedStatement st = carrierInfo.getConnection().prepareStatement(query, generatedCols)) {
            st.setDate(1, new java.sql.Date(p.getAcceptanceDate().getTime()));
            st.setLong(2, p.getStartPoint().getId());
            st.setLong(3, p.getEndPoint().getId());
            st.executeUpdate();

            ResultSet keySet = st.getGeneratedKeys();
            keySet.next();
            p.setId(keySet.getLong(1));
            keySet.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public Package getPackage(Long packageId) {
        String query = "select * from " + carrierInfo.getUser() + ".PACKAGES where id=" + String.valueOf(packageId);
        try (Statement st = carrierInfo.getConnection().createStatement();
             ResultSet rs = st.executeQuery(query)) {
            rs.next();
            Package aPackage = new Package();
            aPackage.setId(rs.getLong(1));
            aPackage.setAcceptanceDate(rs.getDate(2));
            aPackage.getStartPoint().setId(rs.getLong(3));
            aPackage.getEndPoint().setName(rs.getString(4));
            return aPackage;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Boolean saveRoute(Package aPackage, List<RoutePoint> route) {
        String query = "insert into " + carrierInfo.getUser() + ".PLANNDED_ROUTES values(?,?,?)";
        try (PreparedStatement st = carrierInfo.getConnection().prepareStatement(query)) {
            for (int i = 0; i < route.size(); i++) {
                st.setLong(1, aPackage.getId());
                st.setInt(2, i + 1);
                st.setLong(3, route.get(i).getId());
                st.executeUpdate();
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Boolean setState(java.util.Date operationDate, Package aPackage, Status status, RoutePoint routePoint) {
        String updateStatus = "insert into " + carrierInfo.getUser() + ".DELIVERY_STATUS values(?,?,?,?)";
        try (PreparedStatement st = carrierInfo.getConnection().prepareStatement(updateStatus)) {
            st.setDate(1, new java.sql.Date(operationDate.getTime()));
            st.setLong(2, aPackage.getId());
            st.setLong(3, routePoint.getId());
            st.setLong(4, status.label);
            st.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Status getCurrentStatus(Package aPackage) {
        String query = getStatusQuery();
        query += " where d.package_ref=" + String.valueOf(aPackage.getId());
        try (Statement st = carrierInfo.getConnection().createStatement();
             ResultSet rs = st.executeQuery(query)) {
            if (!rs.next())
                return null;
            return Status.get(rs.getLong(4));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public RoutePoint getCurrentPoint(Package aPackage) {
        String query = getStatusQuery();
        query += " where d.package_ref=" + String.valueOf(aPackage.getId());
        query+=" and dates.package_ref=" + String.valueOf(aPackage.getId());
        try (Statement st = carrierInfo.getConnection().createStatement();
             ResultSet rs = st.executeQuery(query)) {
            if (!rs.next())
                return null;
            Long pointId = rs.getLong(1);
            return carrierInfo.getRoutePointsService().getRoutePoint(pointId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Package> packagesInStatus(Status status) {
        List<Package> packages = new ArrayList<>();
        String queryStatus = getStatusQuery();
        queryStatus += " where d.status_ref =" + String.valueOf(status.label);
        String query = "select * from " + carrierInfo.getUser() + ".PACKAGES p where p.id in ( select q.package_ref from (" +
                queryStatus + ") q)";
        packages = executeQuery(query);
        return packages;
    }

    public List<Package> packagesInStatus(Status status, java.util.Date beforeDate) {
        List<Package> packages = new ArrayList<>();

        String queryStatus = getStatusQuery();
        queryStatus += " where d.status_ref =" + String.valueOf(status.label);
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        queryStatus += " and d.operation_date<to_date('" + df.format(beforeDate) + "','DD-MM-YYYY')";
        String query = "select * from " + carrierInfo.getUser() + ".PACKAGES p where p.id in ( select q.package_ref from (" +
                queryStatus + ") q)";
        packages = executeQuery(query);
        return packages;
    }

    private List<Package> executeQuery(String query) {
        List<Package> packages = new ArrayList<>();
        try (Statement st = carrierInfo.getConnection().createStatement();
             ResultSet rs = st.executeQuery(query)) {
            if (!rs.next()) return packages;
            while (rs.next()) {
                Package p = new Package();
                p.setId(rs.getLong(1));
                p.setAcceptanceDate(rs.getDate(2));
                p.setStartPoint(carrierInfo.getRoutePointsService().getRoutePoint(rs.getLong(3)));
                p.setEndPoint(carrierInfo.getRoutePointsService().getRoutePoint(rs.getLong(4)));
                packages.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return packages;
    }

    public String getStatusQuery() {
        String query = "select d.* from " + carrierInfo.getUser() + ".delivery_status d left join( \n" +
                "select package_ref,MAX(operation_date) as max_date from " + carrierInfo.getUser() + ".delivery_status\n" +
                "group by package_ref) dates on dates.max_date=d.operation_date\n";
        return query;
    }

}
