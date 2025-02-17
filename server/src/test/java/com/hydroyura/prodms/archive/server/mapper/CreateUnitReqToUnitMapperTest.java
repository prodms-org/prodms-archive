package com.hydroyura.prodms.archive.server.mapper;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.hydroyura.prodms.archive.client.model.enums.UnitStatus;
import com.hydroyura.prodms.archive.client.model.enums.UnitType;
import com.hydroyura.prodms.archive.client.model.req.CreateUnitReq;
import java.util.Objects;
import org.junit.jupiter.api.Test;

class CreateUnitReqToUnitMapperTest {

    private final CreateUnitReqToUnitMapper mapper = new CreateUnitReqToUnitMapperImpl();

    @Test
    void mapTest() {
        var type = UnitType.PART;
        var status = UnitStatus.TEST;
        var name = "NAME";
        var number = "NUMBER";
        var additional = "ADDITIONAL";

        var createUnit = CreateUnitReq.builder()
            .name(name)
            .number(number)
            .status(status)
            .type(type)
            .additional(additional)
            .build();

        var unit = mapper.toDestination(createUnit);

        assertTrue(
            Objects.equals(type.getCode(), unit.getType()) &&
            Objects.equals(status.getCode(), unit.getStatus()) &&
            Objects.equals(name, unit.getName()) &&
            Objects.equals(number, unit.getNumber()) &&
            Objects.equals(additional, unit.getAdditional()) &&
            Objects.nonNull(unit.getCreatedAt()) &&
            Objects.nonNull(unit.getUpdatedAt()) &&
            Objects.equals(unit.getCreatedAt(), unit.getUpdatedAt()) &&
            unit.getIsActive() &&
            Objects.equals(1, unit.getVersion())
        );
    }
}