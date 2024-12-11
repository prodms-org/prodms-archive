package com.hydroyura.prodms.archive.server.service;

import com.hydroyura.prodms.archive.client.model.req.CreateUnitReq;
import com.hydroyura.prodms.archive.client.model.req.ListUnitsReq;
import com.hydroyura.prodms.archive.client.model.req.PatchUnitReq;
import com.hydroyura.prodms.archive.client.model.res.GetUnitRes;
import com.hydroyura.prodms.archive.client.model.res.ListUnitsRes;
import com.hydroyura.prodms.archive.server.db.EntityManagerProvider;
import com.hydroyura.prodms.archive.server.db.entity.Unit;
import com.hydroyura.prodms.archive.server.db.entity.UnitHist;
import com.hydroyura.prodms.archive.server.db.repository.UnitHistRepository;
import com.hydroyura.prodms.archive.server.db.repository.UnitRepository;
import com.hydroyura.prodms.archive.server.mapper.CreateUnitReqToUnitMapper;
import com.hydroyura.prodms.archive.server.mapper.UnitToGetUnitResMapper;
import com.hydroyura.prodms.archive.server.mapper.UnitToUnitHistMapper;
import jakarta.persistence.EntityTransaction;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UnitService {

    private final CreateUnitReqToUnitMapper createUnitReqToUnitMapper;
    private final UnitToUnitHistMapper unitToUnitHistMapper;
    private final UnitRepository unitRepository;
    private final UnitHistRepository unitHistRepository;
    private final EntityManagerProvider entityManagerProvider;
    private final UnitToGetUnitResMapper unitToGetUnitResMapper;


    public ListUnitsRes list(ListUnitsReq req) {
        EntityTransaction transaction = entityManagerProvider.getTransaction();
        try {
            transaction.begin();
            Collection<GetUnitRes> units = unitRepository.list(req)
                .stream()
                .map(unitToGetUnitResMapper::toDestination)
                .toList();
            transaction.commit();
            return ListUnitsRes.builder()
                .units(units)
                .build();
        } catch (Exception e) {
            transaction.rollback();
            //TODO: create custom ex
            throw new RuntimeException();
        }
    }

    public void create(CreateUnitReq req) {
        EntityTransaction transaction = entityManagerProvider.getTransaction();
        try {
            transaction.begin();
            var unit = createUnitReqToUnitMapper.toDestination(req);
            unitRepository.create(unit);
            var unitHist = unitToUnitHistMapper.toDestination(unit, 1);
            unitHistRepository.create(unitHist);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            //TODO: create custom ex
            throw new RuntimeException();
        }

    }

    public void create(List<CreateUnitReq> units) {
        EntityTransaction transaction = entityManagerProvider.getTransaction();
        try {
            transaction.begin();
            units
                .stream()
                .map(createUnitReqToUnitMapper::toDestination)
                .peek(unitRepository::create)
                .map(unit -> unitToUnitHistMapper.toDestination(unit, 1))
                .forEach(unitHistRepository::create);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            //TODO: create custom ex
            throw new RuntimeException();
        }
    }

    public void delete(String number) {
        EntityTransaction transaction = entityManagerProvider.getTransaction();
        try {
            transaction.begin();
            unitRepository.delete(number);

            var unitHist = new UnitHist();
            unitHistRepository.create(unitHist);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            //TODO: create custom ex
            throw new RuntimeException();
        }
    }

    public GetUnitRes get(String number) {
        // TODO: custom ex
        return unitRepository.get(number)
            .map(unitToGetUnitResMapper::toDestination)
            .orElseThrow(() -> new RuntimeException("Not found"));
    }

    public void patch(String number, PatchUnitReq req) {
        EntityTransaction transaction = entityManagerProvider.getTransaction();
        try {
            transaction.begin();
            var pachedUnit = unitRepository.get(number)
                .map(unit -> populateExistentUnitWithNewValues(unit, req))
                // TODO: custom ex
                .orElseThrow(() -> new RuntimeException("Ex"));
            patchUnitAndSaveHistory(pachedUnit);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            //TODO: create custom ex
            throw new RuntimeException();
        }
    }

    private Unit populateExistentUnitWithNewValues(Unit unit, PatchUnitReq req) {
        unit.setVersion(unit.getVersion() + 1);
        unit.setUpdatedAt(Instant.now().getEpochSecond());
        unit.setStatus(req.getStatus());
        unit.setName(req.getName());
        unit.setAdditional(req.getAdditional());
        return unit;
    }

    private void patchUnitAndSaveHistory(Unit unit) {
        unitRepository.create(unit);
        var unitHist = unitToUnitHistMapper.toDestination(unit, 2);
        unitHistRepository.create(unitHist);
    }

}
