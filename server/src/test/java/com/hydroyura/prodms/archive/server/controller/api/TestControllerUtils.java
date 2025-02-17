package com.hydroyura.prodms.archive.server.controller.api;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import java.util.Map;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TestControllerUtils {

    public static final String UNIT_NUMBER_1 = "unit-number-1";
    public static final String UNIT_NAME_1 = "unit-name-1";



    public static final JavaType MAP_TYPE_FOR_PARAMS = TypeFactory.defaultInstance().constructMapType(
        Map.class,
        TypeFactory.defaultInstance().constructType(String.class),
        TypeFactory.defaultInstance().constructType(Object.class));

}
