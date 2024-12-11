package com.hydroyura.prodms.archive.server.db.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "units")
public class Unit {

    @Id
    private String number;
    private String name;
    private Integer type;
    private Integer status;
    private Integer version;

    @Column(name = "created_at")
    private Long createdAt;

    @Column(name = "updated_at")
    private Long updatedAt;

    private String additional;

    @Column(name = "is_active")
    private Boolean isActive;

}
