package ru.home.post.writer.entties;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;

@Entity
@Table(name = "PLANNED_ROUTES")
public class PlannedPoint implements Comparable{
    @SequenceGenerator(name = "planned_routes_generator", sequenceName = "PLANNED_ROUTES_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "package_generator")
    @Column(name = "ID")
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PACKAGE_REF")
    private Package aPackage;
    @Column(name = "POINT_NUMBER")
    private Integer pointNumber;
    @Column(name = "DELIVERY_POINT")
    private DeliveryPoint deliveryPoint;

    @Override
    public int compareTo(Object o) {
        PlannedPoint another=(PlannedPoint)o;
        return ((PlannedPoint) o).pointNumber.compareTo(another.pointNumber);
    }
}
