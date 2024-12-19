package com.hydroyura.prodms.archive.server.db.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "rates")
@IdClass(RateId.class)
public class Rate {

    @Id
    @Column(name = "unit_number")
    private String unitNumber;

    @Id
    @Column(name = "assembly_number")
    private String assemblyNumber;

    private Unit assembly;
    private Unit unit;
    private Integer rate;
    private Integer version;

    @Column(name = "created_at")
    private Long createdAt;

    @Column(name = "updated_at")
    private Long updatedAt;

    @Column(name = "is_active")
    private Boolean isActive;

}
