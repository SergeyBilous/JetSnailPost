package ru.home.post.writer.commons;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PackageService {
    public static Package createPackage() {
        return new Package();
    }

    public static Boolean savePackage(CarrierInfo carrierInfo, Package p) {

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
}
