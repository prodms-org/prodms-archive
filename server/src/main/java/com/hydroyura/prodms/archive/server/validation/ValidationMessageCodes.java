package com.hydroyura.prodms.archive.server.validation;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ValidationMessageCodes {

    public static final String DEFAULT_MSG = "INVALID FILED";

    public static final String CREATE_UNIT_NUMBER_EMPTY = "validation.createunit.number.empty";
    public static final String CREATE_UNIT_NUMBER_REGEX = "validation.createunit.number.regex";
    public static final String CREATE_UNIT_NAME_EMPTY = "validation.createunit.name.empty";
    public static final String CREATE_UNIT_NAME_REGEX = "validation.createunit.name.regex";
    public static final String CREATE_UNIT_TYPE_EMPTY = "validation.createunit.type.empty";
    public static final String CREATE_UNIT_STATUS_EMPTY = "validation.createunit.status.empty";

}
