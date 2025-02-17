package com.hydroyura.prodms.archive.server.db.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.Collection;
import lombok.Data;
import org.hibernate.annotations.LazyGroup;

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

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "number")
    private Collection<UnitHist> history;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "assembly")
    private Collection<Rate> useIn;

}
