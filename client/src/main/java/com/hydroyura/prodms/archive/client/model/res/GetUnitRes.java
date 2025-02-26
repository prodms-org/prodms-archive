package com.hydroyura.prodms.archive.client.model.res;

import com.hydroyura.prodms.archive.client.model.enums.UnitStatus;
import com.hydroyura.prodms.archive.client.model.enums.UnitType;
import com.hydroyura.prodms.files.server.api.enums.DrawingType;
import java.time.Instant;
import java.util.Collection;
import java.util.Map;
import lombok.Data;

@Data
public class GetUnitRes {
    private String number;
    private String name;
    private UnitType type;
    private UnitStatus status;
    private Integer version;
    private Instant createdAt;
    private Instant updatedAt;
    private String additional;
    private Collection<UnitHist> history;
    private Map<DrawingType, String> drawings;

    @Data
    public static class UnitHist {
        private String number;
        private Integer version;
        private Integer operation;
        private String json;
    }
}
