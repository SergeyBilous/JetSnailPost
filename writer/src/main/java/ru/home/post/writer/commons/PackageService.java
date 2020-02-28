package ru.home.post.writer.commons;

import java.sql.*;
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
        try (PreparedStatement st = carrierInfo.getConnection().prepareStatement(query)) {
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
}
