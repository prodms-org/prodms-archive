package com.hydroyura.prodms.archive.server.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hydroyura.prodms.archive.server.db.entity.Unit;
import com.hydroyura.prodms.archive.server.db.entity.UnitHist;
import org.mapstruct.Mapper;

@Mapper
public abstract class UnitToUnitHistMapper implements OneSideMapper<Unit, UnitHist> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public UnitHist toDestination(Unit source, Integer operation) {
        var unitHist = toDestination(source);
        unitHist.setOperation(operation);
        try {
            unitHist.setJson(objectMapper.writeValueAsString(source));
        } catch (JsonProcessingException e) {
            //TODO: add custom ex
            throw new RuntimeException(e);
        }
        return unitHist;
    }

}
