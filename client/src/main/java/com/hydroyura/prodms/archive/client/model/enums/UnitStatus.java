package com.hydroyura.prodms.archive.client.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum UnitStatus {

    DESIGN(1), TEST(2), PRODUCTION(3), OTHER(4);

    UnitStatus(Integer code) {
        this.code = code;
    }

    //@JsonValue
    private final Integer code;

}
