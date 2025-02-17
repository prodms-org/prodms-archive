package com.hydroyura.prodms.archive.server.mapper;

import com.hydroyura.prodms.archive.client.model.res.GetUnitRes;
import com.hydroyura.prodms.archive.client.model.enums.UnitStatus;
import com.hydroyura.prodms.archive.client.model.enums.UnitType;
import com.hydroyura.prodms.archive.server.db.entity.Unit;
import java.time.Instant;
import java.util.Arrays;
import org.mapstruct.Mapper;

@Mapper
public interface UnitToGetUnitResMapper extends OneSideMapper<Unit, GetUnitRes> {

    default Instant convertTime(Long utc) {
        return Instant.ofEpochSecond(utc);
    }

    default UnitType convertType(Integer code) {
        // TODO: create custom ex
        return Arrays.stream(UnitType.values())
            .filter(value -> value.getCode().equals(code))
            .findFirst()
            .orElseThrow(() -> new RuntimeException(""));
    }

    default UnitStatus convertStatus(Integer code) {
        // TODO: create custom ex
        return Arrays.stream(UnitStatus.values())
            .filter(value -> value.getCode().equals(code))
            .findFirst()
            .orElseThrow(() -> new RuntimeException(""));
    }

}
