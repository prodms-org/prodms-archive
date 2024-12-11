package com.hydroyura.prodms.archive.client.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum UnitType {

    PART(1), ASSEMBLY(2), BUY(3), STANDARD(4), OTHER(5);

    UnitType(Integer code) {
        this.code = code;
    }

    //@JsonValue
    private final Integer code;

}
