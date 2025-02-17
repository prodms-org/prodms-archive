package com.hydroyura.prodms.archive.client.model.req;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hydroyura.prodms.archive.client.model.config.objectmapper.UnitTypeDeserializer;
import com.hydroyura.prodms.archive.client.model.enums.UnitStatus;
import com.hydroyura.prodms.archive.client.model.enums.UnitType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateUnitReq {
    private String number;
    private String name;

    //@JsonDeserialize(using = UnitTypeDeserializer.class)
    private UnitType type;

    private UnitStatus status;
    private String additional;
}
