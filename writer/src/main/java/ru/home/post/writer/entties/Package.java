package ru.home.post.writer.entties;

import javax.persistence.*;
import java.util.Date;
import java.util.TreeSet;

@Entity
@Table(name="PACKAGES",schema = "carrier")
public class Package {
    @Id
    @SequenceGenerator(name="packages_generator",sequenceName = "PACKAGES_SEQ1",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "packages_generator")
    @Column(name = "ID")
    private Long id;
    @Column(name="ACCEPTANCE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date acceptanceDate;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "START_POINT")
    private DeliveryPoint startPoint;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "END_POINT")
    private DeliveryPoint endPoint;
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name="PACKAGE_REF")
    private TreeSet<PlannedPoint> routePlan;
}
