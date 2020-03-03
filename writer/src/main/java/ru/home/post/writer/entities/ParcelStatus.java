package ru.home.post.writer.entities;

public enum ParcelStatus {
    ACCEPTED(1L),
    WAITING_FOR_TRANSPORT(2L),
EN_ROUTE(3L),
READY_FOR_DELIVERY(4L)  ;
    public Long label;
private ParcelStatus(Long label){this.label=label;}
}
