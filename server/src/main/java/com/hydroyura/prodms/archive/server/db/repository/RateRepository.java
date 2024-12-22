package com.hydroyura.prodms.archive.server.db.repository;

import com.hydroyura.prodms.archive.server.db.entity.RateId;

public interface RateRepository {

    void create(RateId id, Integer count);
    void patchCount(RateId id, Integer newCount);
    void delete(RateId id);

}
