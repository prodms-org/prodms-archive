package com.hydroyura.prodms.archive.server.mapper;

import com.hydroyura.prodms.archive.client.model.enums.UnitStatus;
import com.hydroyura.prodms.archive.client.model.enums.UnitType;
import com.hydroyura.prodms.archive.client.model.req.CreateUnitReq;
import com.hydroyura.prodms.archive.server.db.entity.Unit;
import java.time.Instant;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper
public interface CreateUnitReqToUnitMapper extends OneSideMapper<CreateUnitReq, Unit> {

    Integer DEFAULT_VERSION = 1;
    Boolean DEFAULT_IS_ACTIVE = Boolean.TRUE;

    default Integer statusToCode(UnitStatus status) {
        return status == null ? null : status.getCode();
    }

    default Integer typeToCode(UnitType type) {
        return type == null ? null : type.getCode();
    }

    @AfterMapping
    default void populateWithDefaultValues(@MappingTarget Unit unit) {
        Long now = Instant.now().getEpochSecond();
        unit.setCreatedAt(now);
        unit.setUpdatedAt(now);
        unit.setVersion(DEFAULT_VERSION);
        unit.setIsActive(DEFAULT_IS_ACTIVE);
    }

}
