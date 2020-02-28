package ru.home.post.writer.commons;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateService {
    private CarrierInfo carrierInfo;
    private Date date;
    private Integer iteration;

    public DateService(CarrierInfo carrierInfo) {
        this.carrierInfo=carrierInfo;
        String query = "select * from " + carrierInfo.getUser() + ".TIMESETTINGS "; // В этой таблице всегда 1 запись
        try (Statement st = carrierInfo.getConnection().createStatement();
             ResultSet rs = st.executeQuery(query)) {
            if (!rs.next()) {
                insertRow();
            } else {
                date = rs.getDate(1);
                iteration = rs.getInt(2);
            }

        } catch (SQLException | ParseException e) {
            e.printStackTrace();
        }
    }

    public void newDate() {
        iteration++;
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH, iteration);
        date = c.getTime();
        String updateQuery = "update " + carrierInfo.getUser() + ".TIMESETTINGS set ITERATION_NUMBER=? where 1=1";
        try (PreparedStatement st = carrierInfo.getConnection().prepareStatement(updateQuery)) {
            st.setInt(1, iteration);
            st.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Date getDate() {
        return date;
    }

    public Integer getIteration() {
        return iteration;
    }

    private void insertRow() throws ParseException {
        String insertQuery = "insert into " + carrierInfo.getUser() + ".TIMESETTINGS values(?,?) ";
        Date startDate = new Date();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        startDate = df.parse(df.format(startDate));
        try (PreparedStatement insertSt = carrierInfo.getConnection().prepareStatement(insertQuery)) {
            insertSt.setDate(1, new java.sql.Date(startDate.getTime()));
            insertSt.setInt(2, 0);
            insertSt.executeUpdate();
            date = startDate;
            iteration = 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
