package ru.home.post.writer.entities;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="CURRENT_DELIVERY_STATUS",schema="CARRIER")
public class CurrentDeliveryStatus {
    @Id
    @Column(name="ID")
    private Long id;
    @Column(name="OPERATION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date operationDate;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PACKAGE_REF", referencedColumnName = "ID")
    private Parcel parcel;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "POINT_REF")
    private DeliveryPoint deliveryPoint;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "STATUS_REF", referencedColumnName = "ID")
    private Statuses status;
public CurrentDeliveryStatus(){}

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        CurrentDeliveryStatus that = (CurrentDeliveryStatus) o;

        return new EqualsBuilder()
                .append(id, that.id)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .toHashCode();
    }
}
