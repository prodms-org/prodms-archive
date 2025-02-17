package com.hydroyura.prodms.archive.server.db.repository;

import com.hydroyura.prodms.archive.server.db.entity.RateId;
import com.hydroyura.prodms.archive.server.dto.RateDto;
import java.util.Collection;

public interface RateRepository {

    void create(String assemblyNumber, String unitNumber, Integer count);
    void patchCount(String assemblyNumber, String unitNumber, Integer newCount);
    void delete(String assemblyNumber, String unitNumber);
    Collection<RateDto> getRates(String assembly);

}
