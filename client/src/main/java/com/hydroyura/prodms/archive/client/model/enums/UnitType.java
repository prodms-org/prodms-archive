package com.hydroyura.prodms.archive.client.model.enums;

import lombok.Getter;

@Getter
public enum UnitType {

    PART(1), ASSEMBLY(2), VZK(3), OTHER(4);

    UnitType(Integer code) {
        this.code = code;
    }

    private final Integer code;

}
