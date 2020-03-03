package ru.home.post.writer.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "TIMESETTINGS",schema="CARRIER")
public class TimeSettings {
    @Id
    @Column(name = "ID")
    private Long id;
    @Column(name = "START_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Column(name = "ITERATION_NUMBER")
    private Long iterationNumber;
    public TimeSettings(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getIterationNumber() {
        return iterationNumber;
    }

    public void setIterationNumber(Long iterationNumber) {
        this.iterationNumber = iterationNumber;
    }
}
