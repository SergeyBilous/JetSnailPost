package ru.home.post.writer.entities;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;

@Entity
@Table(name = "PLANNED_ROUTES")
public class PlannedPoint implements Comparable<PlannedPoint> {
    @Id
    @SequenceGenerator(name = "planned_routes_generator", sequenceName = "PLANNED_ROUTES_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "planned_routes_generator")
    @Column(name = "ID")
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PACKAGE_REF")
    private Parcel parcel;
    @Column(name = "POINT_NUMBER")
    private Integer pointNumber;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "DELIVERY_POINT")
    private DeliveryPoint deliveryPoint;

    public PlannedPoint() {
    }

    @Override
    public int compareTo(PlannedPoint o) {

        return pointNumber.compareTo(o.pointNumber);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Parcel getParcel() {
        return parcel;
    }

    public void setParcel(Parcel parcel) {
        this.parcel = parcel;
    }

    public Integer getPointNumber() {
        return pointNumber;
    }

    public void setPointNumber(Integer pointNumber) {
        this.pointNumber = pointNumber;
    }

    public DeliveryPoint getDeliveryPoint() {
        return deliveryPoint;
    }

    public void setDeliveryPoint(DeliveryPoint deliveryPoint) {
        this.deliveryPoint = deliveryPoint;
    }
}
