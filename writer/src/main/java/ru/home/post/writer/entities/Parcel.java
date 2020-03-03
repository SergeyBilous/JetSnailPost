package ru.home.post.writer.entities;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;

@Entity
@Table(name = "PACKAGES", schema = "carrier")
public class Parcel {
    @Id
    @SequenceGenerator(name = "packages_generator", sequenceName = "PACKAGES_SEQ1", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "packages_generator")
    @Column(name = "ID")
    private Long id;

    @Column(name = "ACCEPTANCE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date acceptanceDate;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "START_POINT")
    private DeliveryPoint startPoint;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "END_POINT")
    private DeliveryPoint endPoint;

    public List<PlannedPoint> getRoutePlan() {
        return routePlan;
    }

    public void setRoutePlan(List<PlannedPoint> routePlan) {
        this.routePlan = routePlan;
    }

    @OneToMany(mappedBy = "parcel")
    private List<PlannedPoint> routePlan;

    public Parcel() {
    }

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

    public DeliveryPoint getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(DeliveryPoint startPoint) {
        this.startPoint = startPoint;
    }

    public DeliveryPoint getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(DeliveryPoint endPoint) {
        this.endPoint = endPoint;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Parcel parcel = (Parcel) o;

        return new EqualsBuilder()
                .append(id, parcel.id)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .toHashCode();
    }
}
