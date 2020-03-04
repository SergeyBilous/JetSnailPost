package ru.home.post.writer.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class CarrierInfo {
    private String user;
    Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public String getUser() {
        return user;
    }

    public CarrierInfo(String url,  String user, String password) {

        this.user = user;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connection= DriverManager.getConnection(url,user,password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

    }


}
