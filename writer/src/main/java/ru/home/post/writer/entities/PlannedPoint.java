package ru.home.post.writer.entities;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;

@Entity
@Table(name = "PLANNED_ROUTES")
public class PlannedPoint implements Comparable<PlannedPoint>{
    @Id
    @SequenceGenerator(name = "planned_routes_generator", sequenceName = "PLANNED_ROUTES_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "package_generator")
    @Column(name = "ID")
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PACKAGE_REF")
    private Parcel parcel;
    @Column(name = "POINT_NUMBER")
    private Integer pointNumber;
    @Column(name = "DELIVERY_POINT")
    private DeliveryPoint deliveryPoint;

    @Override
    public int compareTo(PlannedPoint o) {

        return pointNumber.compareTo(o.pointNumber);
    }
}
