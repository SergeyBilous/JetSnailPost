package ru.home.post.writer.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "DELIVERY_STATUS", schema = "carrier")
public class DeliveryStatus {
    @Id
    @SequenceGenerator(name = "delivery_status_generator", sequenceName = "DELIVERY_STATUS_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "delivery_status_generator")
    @Column(name = "ID")
    private Long id;
    @Temporal(TemporalType.TIMESTAMP)
    private Date operationDate;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "PACKAGE_REF", referencedColumnName = "ID")
    private Parcel parcel;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "POINT_REF")
    private DeliveryPoint deliveryPoint;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "STATUS_REF", referencedColumnName = "ID")
    private Statuses status;

    public DeliveryStatus() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getOperationDate() {
        return operationDate;
    }

    public void setOperationDate(Date operationDate) {
        this.operationDate = operationDate;
    }

    public Parcel getParcel() {
        return parcel;
    }

    public void setParcel(Parcel parcel) {
        this.parcel = parcel;
    }

    public DeliveryPoint getDeliveryPoint() {
        return deliveryPoint;
    }

    public void setDeliveryPoint(DeliveryPoint deliveryPoint) {
        this.deliveryPoint = deliveryPoint;
    }

    public Statuses getStatus() {
        return status;
    }

    public void setStatus(Statuses status) {
        this.status = status;
    }
}
