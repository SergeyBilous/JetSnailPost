package ru.home.post.writer.commons;

import java.util.Date;

public class Package {
    private Long id;
    private Date acceptanceDate;
    private RoutePoint startPoint;
    private RoutePoint endPoint;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getAcceptanceDate() {
        return acceptanceDate;
    }

    public void setAcceptanceDate(Date acceptanceDate) {
        this.acceptanceDate = acceptanceDate;
    }

    public RoutePoint getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(RoutePoint startPoint) {
        this.startPoint = startPoint;
    }

    public RoutePoint getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(RoutePoint endPoint) {
        this.endPoint = endPoint;
    }
}
