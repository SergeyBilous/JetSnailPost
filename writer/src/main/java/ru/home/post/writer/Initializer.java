package ru.home.post.writer;


import ru.home.post.writer.config.CarrierInfo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.Scanner;

public class Initializer {
    private static CarrierInfo carrierInfo;

    public Initializer() {
    }

    public Initializer(CarrierInfo carrierInfo) {
        this.carrierInfo = carrierInfo;
    }

    public static Boolean truncateTables() {
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

    public static Boolean loadData(String tableName, File contentFile) {
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

    public static void main(String[] args) throws IOException {
        if(args.length==0){
            System.out.println("Nothing to do");
            return;
        }
        if(args[0].equalsIgnoreCase("ALL")){
            System.out.println("Clear all data, are you shure(Y/N, defaul N)?");
            try(Scanner keybScanner=new Scanner(System.in)){
                String response=keybScanner.nextLine();
                if(!response.toUpperCase().startsWith("Y"))
                    return;
                clearAll();
            }
        }else{
            if(args[0].equalsIgnoreCase("MOVES")){
                clearMoves();
            }
        }


    }
    public static void clearAll() throws IOException {
        connect();
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
    public static void clearMoves() throws IOException {
        connect();
        String query="delete from "+carrierInfo.getUser()+".DELIVERY_STATUS where status_ref>1";
        try(Statement st=carrierInfo.getConnection().createStatement()){
            st.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void connect() throws IOException {
        Properties prop=new Properties();
        String propFileName="application.properties";
        InputStream inputStream=Initializer.class.getClassLoader().getResourceAsStream(propFileName);
        prop.load(inputStream);
        carrierInfo = new CarrierInfo(prop.getProperty("spring.datasource.url"),
                prop.getProperty("spring.datasource.username"),
                prop.getProperty("spring.datasource.password"));
    }
}
