package com.hydroyura.prodms.archive.server.db.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "units_history")
@IdClass(UnitHistId.class)
public class UnitHist {

    @Id
    private String number;

    @Id
    private Integer version;

    private Integer operation;
    private String json;

}
