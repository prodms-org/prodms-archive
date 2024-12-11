package com.hydroyura.prodms.archive.client.model.enums;

import java.util.Arrays;
import java.util.Locale;
import lombok.experimental.UtilityClass;

@UtilityClass
public class EnumUtils {

    public static Integer getUnitTypeCodeByString(String value) {
        String upperCaseValue = value.toUpperCase(Locale.getDefault());
        // TODO: custom error
        return Arrays.stream(UnitType.values())
            .filter(arg -> upperCaseValue.equals(arg.name()))
            .findFirst()
            .map(UnitType::getCode)
            .orElseThrow(() -> new RuntimeException("Can not find ...."));
    }

    public static Integer getUnitStatusCodeByString(String value) {
        String upperCaseValue = value.toUpperCase(Locale.getDefault());
        // TODO: custom error
        return Arrays.stream(UnitStatus.values())
            .filter(arg -> upperCaseValue.equals(arg.name()))
            .findFirst()
            .map(UnitStatus::getCode)
            .orElseThrow(() -> new RuntimeException("Can not find ...."));
    }



}
