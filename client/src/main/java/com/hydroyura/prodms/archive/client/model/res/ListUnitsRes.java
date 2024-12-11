package com.hydroyura.prodms.archive.client.model.res;

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

    private Collection<GetUnitRes> units;

}
