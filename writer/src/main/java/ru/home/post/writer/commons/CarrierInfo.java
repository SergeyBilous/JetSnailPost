package ru.home.post.writer.commons;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class CarrierInfo {
    private String host, dbname, user, password;
    private Connection con = null;

    public CarrierInfo(String host, String dbname, String user, String password) {
        this.host = host;
        this.dbname = dbname;
        this.user = user;

        this.password = password;

    }

    public Connection getConnection() {
        if (con == null) {
            try {
                Class.forName("oracle.jdbc.driver.OracleDriver");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            String url="jdbc:oracle:thin@hadoop.home.ru:1521/"+dbname;
            try {
                con= DriverManager.getConnection(url,this.user,this.password);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return con;
    }

    public String getUser() {
        return user;
    }
}
