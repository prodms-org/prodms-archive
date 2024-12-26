package com.hydroyura.prodms.archive.server.db.repository;

import com.hydroyura.prodms.archive.server.db.entity.RateId;

public interface RateRepository {

    void create(String assemblyNumber, String unitNumber, Integer count);
    void patchCount(String assemblyNumber, String unitNumber, Integer newCount);
    void delete(String assemblyNumber, String unitNumber);

}
