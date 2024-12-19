package com.hydroyura.prodms.archive.client.model.res;

import com.hydroyura.prodms.archive.client.model.enums.UnitStatus;
import com.hydroyura.prodms.archive.client.model.enums.UnitType;
import java.util.Collection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ListUnitsRes {

    private Collection<UnitRes> units;

    @Data
    public static class UnitRes {
        private String number;
        private String name;
        private UnitType type;
        private UnitStatus status;
        private Integer version; 
    }

}
