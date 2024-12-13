package com.hydroyura.prodms.archive.server.db.repository;

import lombok.experimental.UtilityClass;

@UtilityClass
public class RepositoryTestUtils {

    public static final String UNIT_NAME_1 = "NAME_1";
    public static final String UNIT_NAME_2 = "NAME_2";
    public static final String UNIT_NAME_3 = "NAME_3";
    public static final String UNIT_NAME_4 = "NAME_4";

    public static final String UNIT_NUMBER_1 = "NUMBER_1";
    public static final String UNIT_NUMBER_2 = "NUMBER_2";
    public static final String UNIT_NUMBER_3 = "NUMBER_3";
    public static final String UNIT_NUMBER_4 = "NUMBER_4";


    public static final String UNIT_SQL_SELECT_COUNT_OF_ACTIVE_BY_NUMBER =
        "echo \"SELECT COUNT(number) from units WHERE units.number = '%s' AND units.is_active = 'true';\" | psql -U test-pg-user -d test-archive";
    public static final String UNIT_SQL_TRUNCATE =
        "echo \"TRUNCATE TABLE units;\" | psql -U test-pg-user -d test-archive";

    public static final String UNIT_SQL_CREATE_NUMBER_1 = """
        echo "
            INSERT INTO units (name, number, created_at, updated_at, version, status, type, is_active, additional) \s
            VALUES ('NAME_1', 'NUMBER_1', 100, 100, 1, 1, 1, true, 'some_additional');" | psql -U test-pg-user -d test-archive
   \s""";

    public static final String UNIT_SQL_CREATE_NUMBER_1_NOT_ACTIVE = """
        echo "
            INSERT INTO units (name, number, created_at, updated_at, version, status, type, is_active, additional) \s
            VALUES ('NAME_1', 'NUMBER_1', 100, 100, 1, 1, 1, false, 'some_additional');" | psql -U test-pg-user -d test-archive
   \s""";


}
