package ru.home.post.writer;


import ru.home.post.writer.config.CarrierInfo;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Initializer {
    private CarrierInfo carrierInfo;

    public Initializer() {
    }

    public Initializer(CarrierInfo carrierInfo) {
        this.carrierInfo = carrierInfo;
    }

    public Boolean truncateTables() {
        String[] tablesToTruncate = {"DELIVERY_POINTS", "DELIVERY_STATUS", "PACKAGES", "PLANNED_ROUTES",
                "STATUSES", "TIMESETTINGS"};
        try (Statement st = carrierInfo.getConnection().createStatement()) {
            for (String tableName : tablesToTruncate) {
                System.out.println("Truncating " + tableName);
                String queryText = "truncate table " + carrierInfo.getUser() + "." + tableName;
                st.executeUpdate(queryText);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        try(Statement st=carrierInfo.getConnection().createStatement()){
            String addTime="insert into "+carrierInfo.getUser()+".TIMESETTINGS (ID,START_TIME,ITERATION_NUMBER)values (1,null,null)";
            st.executeUpdate(addTime);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;

    }

    public Boolean loadData(String tableName, File contentFile) {
        String query = "insert into " + carrierInfo.getUser() + "." + tableName + " values(?,?)";
        try (Scanner sc = new Scanner(contentFile);
             PreparedStatement st = carrierInfo.getConnection().prepareStatement(query)) {
            while (sc.hasNextLine()) {
                String[] lineParts = sc.nextLine().split(",");
                st.setLong(1, Long.valueOf(lineParts[0]));
                st.setString(2, lineParts[1]);
                st.executeUpdate();

            }
        } catch (FileNotFoundException | SQLException | NullPointerException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        CarrierInfo carrierInfo = new CarrierInfo("hadoop.home.ru", "logistic1", "CARRIER", "welcome1");
        Initializer initializer = new Initializer(carrierInfo);
        ClassLoader classLoader = initializer.getClass().getClassLoader();

        if (initializer.truncateTables()) {
            File pointsFile = new File(classLoader.getResource("points.csv").getFile());
            if (!initializer.loadData("DELIVERY_POINTS", pointsFile))
                return;
            File statusFile = new File(classLoader.getResource("statuses.csv").getFile());
            initializer.loadData("STATUSES", statusFile);
        }
    }
}
