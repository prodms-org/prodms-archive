package com.hydroyura.prodms.archive.server.db.repository;

import com.hydroyura.prodms.archive.client.model.req.ListUnitsReq;
import com.hydroyura.prodms.archive.server.db.entity.Unit;
import jakarta.persistence.criteria.Predicate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UnitRepository {

    void create(Unit unit);


    Optional<Unit> get(String number);

    void delete(String number);

    Collection<Unit> list(ListUnitsReq filter);

}
