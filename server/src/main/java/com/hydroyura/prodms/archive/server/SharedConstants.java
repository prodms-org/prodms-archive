package com.hydroyura.prodms.archive.server;

import lombok.experimental.UtilityClass;

@UtilityClass
public class SharedConstants {

    public static final String LOG_MSG_UNIT_NOT_FOUND = "Unit with number = [{}] not found";
    public static final String LOG_MSG_RATE_NOT_FOUND = "Rate with unit number = [{}] and assembly number = [{}] not found";


    public static final String EX_MSG_UNIT_DELETE =
        "Can not delete unit with number = [%s]. Possible reasons are unit not exist or already deleted.";
    public static final String EX_MSG_UNIT_PATCH =
        "Can not patch unit with number = [%s]. Possible reasons are unit not exist or nothing to change.";
    public static final String EX_MSG_RATE_FIND_UNIT =
        "Can not find unit with number = [%s] for populating field [%s] in Rate.class.";
    public static final String EX_MSG_RATE_PATCH_COUNT =
        "Can not patch rate with unit number = [%s] and assembly number = [%s]. Current count = [%s] new count = [%s].";
}
