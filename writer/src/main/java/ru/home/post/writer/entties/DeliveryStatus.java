package ru.home.post.writer.entties;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="DELIVERY_STATUS",schema = "carrier")
public class DeliveryStatus {
    @SequenceGenerator(name="delivery_status_generator",sequenceName = "DELIVERY_STATUS_SEQ",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "package_generator")
    @Column(name = "ID")
    private Long id;
    @Temporal(TemporalType.TIMESTAMP)
    private Date operationDate;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PACKAGE_REF")
    private Package aPackage;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "POINT_REF")
    private DeliveryPoint deliveryPoint;

}
