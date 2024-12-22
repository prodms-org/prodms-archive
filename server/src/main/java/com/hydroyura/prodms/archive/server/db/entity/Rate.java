package com.hydroyura.prodms.archive.server.db.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "rates")
@IdClass(RateId.class)
public class Rate {

    @Id
    @ManyToOne
    @JoinColumn(name = "unit_number")
    private Unit unit;

    @Id
    @ManyToOne
    @JoinColumn(name = "assembly_number")
    private Unit assembly;

    private Integer rate;
    private Integer version;

    @Column(name = "created_at")
    private Long createdAt;

    @Column(name = "updated_at")
    private Long updatedAt;

    @Column(name = "is_active")
    private Boolean isActive;

}
