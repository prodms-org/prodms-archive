package com.hydroyura.prodms.archive.client.model.res;

import com.hydroyura.prodms.archive.client.model.enums.UnitStatus;
import com.hydroyura.prodms.archive.client.model.enums.UnitType;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;

@Data
public class GetAssemblyRes {

    private String number;
    private String name;
    private UnitStatus status;
    private Map<UnitType, Map<Object, Integer>> rates = new HashMap<>();

    @Data
    public static class SimpleUnit {
        private String number;
        private String name;
        private UnitStatus status;
        private UnitType type;
    }

}