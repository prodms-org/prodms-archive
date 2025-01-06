package com.hydroyura.prodms.archive.server.service;

import static com.hydroyura.prodms.archive.server.SharedConstants.LOG_MSG_UNIT_DELETE_NOT_FOUND;

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
import com.hydroyura.prodms.archive.server.mapper.UnitToUnitResMapper;
import jakarta.persistence.EntityTransaction;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UnitService {

    private final CreateUnitReqToUnitMapper createUnitReqToUnitMapper;
    private final UnitToUnitHistMapper unitToUnitHistMapper;
    private final UnitRepository unitRepository;
    private final UnitHistRepository unitHistRepository;
    private final EntityManagerProvider entityManagerProvider;
    private final UnitToGetUnitResMapper unitToGetUnitResMapper;
    private final UnitToUnitResMapper unitToUnitResMapper;


    public ListUnitsRes list(ListUnitsReq req) {
        EntityTransaction transaction = entityManagerProvider.getTransaction();
        try {
            transaction.begin();
            Collection<ListUnitsRes.UnitRes> units = unitRepository.list(req)
                .stream()
                .map(unitToUnitResMapper::toDestination)
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

    public Optional<String> delete(String number) {
        EntityTransaction transaction = entityManagerProvider.getTransaction();
        Optional<String> result = Optional.empty();
        try {
            transaction.begin();
            result = unitRepository.delete(number);

            var unitHist = new UnitHist();
            unitHistRepository.create(unitHist);
            transaction.commit();
        } catch (Exception e) {
            log.error(LOG_MSG_UNIT_DELETE_NOT_FOUND.formatted(number), e);
            transaction.rollback();
            result = Optional.empty();
        }
        return result;
    }

    public Optional<GetUnitRes> get(String number) {
        return unitRepository.get(number).map(unitToGetUnitResMapper::toDestination);
    }

    // TODO: refactoring it, need to remove optional.get()
    public Optional<String> patch(String number, PatchUnitReq req) {
        EntityTransaction transaction = entityManagerProvider.getTransaction();
        try {
            transaction.begin();
            var patchedUnit = unitRepository.get(number)
                .map(unit -> populateExistentUnitWithNewValues(unit, req));
            if (patchedUnit.isPresent()) {
                patchUnitAndSaveHistory(patchedUnit.get());
                transaction.commit();
                return Optional.of(number);
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            transaction.rollback();
            return Optional.empty();
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
