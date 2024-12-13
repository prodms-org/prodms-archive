package com.hydroyura.prodms.archive.server;

import lombok.experimental.UtilityClass;

@UtilityClass
public class SharedConstants {
    public static final String LOG_MSG_UNIT_NOT_FOUND = "Unit with number = [{}] not found";


    public static final String EX_MSG_UNIT_DELETE =
        "Can not delete unit with number = [%s]. Possible reasons are unit not exist or already deleted.";
}
