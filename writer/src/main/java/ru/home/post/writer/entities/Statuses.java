package ru.home.post.writer.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="STATUSES",schema="CARRIER")
public class Statuses {
    @Id
    private Long id;
    @Column(name="STATUS_NAME")
    private String name;
    public Statuses(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
