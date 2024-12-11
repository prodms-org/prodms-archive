package com.hydroyura.prodms.archive.client.model.res;

import com.hydroyura.prodms.archive.client.model.enums.UnitStatus;
import com.hydroyura.prodms.archive.client.model.enums.UnitType;
import java.time.Instant;
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
}
