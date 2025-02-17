package com.hydroyura.prodms.archive.server.db.repository;

import static com.hydroyura.prodms.archive.server.SharedConstants.EX_MSG_RATE_FIND_UNIT;
import static com.hydroyura.prodms.archive.server.SharedConstants.EX_MSG_RATE_NOT_EXIST;
import static com.hydroyura.prodms.archive.server.SharedConstants.EX_MSG_RATE_PATCH_COUNT;
import static com.hydroyura.prodms.archive.server.SharedConstants.LOG_MSG_RATE_NOT_FOUND;

import com.hydroyura.prodms.archive.server.db.EntityManagerProvider;
import com.hydroyura.prodms.archive.server.db.entity.Rate;
import com.hydroyura.prodms.archive.server.db.entity.RateId;
import com.hydroyura.prodms.archive.server.db.entity.Unit;
import com.hydroyura.prodms.archive.server.dto.RateDto;
import com.hydroyura.prodms.archive.server.exception.model.db.RateFindUnitException;
import com.hydroyura.prodms.archive.server.exception.model.db.RateNotExistException;
import com.hydroyura.prodms.archive.server.exception.model.db.RatePatchException;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RateRepositoryImpl implements RateRepository {

    private final EntityManagerProvider entityManagerProvider;

    @Override
    public void create(String assemblyNumber, String unitNumber, Integer count) {
        var entityManager = entityManagerProvider.getEntityManager();
        var rate = new Rate();
        rate.setRate(count);
        findAndSetUnit(
            unitNumber,
            rate::setUnit,
            EX_MSG_RATE_FIND_UNIT.formatted(unitNumber, "Unit")
        );

        // TODO: validate if it really assembly
        findAndSetUnit(
            assemblyNumber,
            rate::setAssembly,
            EX_MSG_RATE_FIND_UNIT.formatted(assemblyNumber, "Assembly")
        );
        Long now = Instant.now().getEpochSecond();
        rate.setCreatedAt(now);
        rate.setUpdatedAt(now);
        // TODO: replace to settings
        rate.setVersion(1);
        rate.setIsActive(Boolean.TRUE);
        entityManager.persist(rate);
    }

    @Override
    public void patchCount(String assemblyNumber, String unitNumber, Integer newCount) {
        var id = new RateId();
        id.setAssembly(assemblyNumber);
        id.setUnit(unitNumber);
        Rate rate = entityManagerProvider.getEntityManager().find(Rate.class, id);
        if (rate == null || !rate.getIsActive()) {
            log.warn(LOG_MSG_RATE_NOT_FOUND, id.getUnit(), id.getAssembly());
            throw new RateNotExistException(EX_MSG_RATE_NOT_EXIST.formatted(id.getAssembly(), id.getUnit()));
        }
        if (rate.getRate().equals(newCount)) {
            throw new RatePatchException(EX_MSG_RATE_PATCH_COUNT.formatted(
                id.getUnit(),
                id.getAssembly(),
                rate.getRate(),
                newCount)
            );
        }
        var now = Instant.now().getEpochSecond();
        rate.setRate(newCount);
        rate.setUpdatedAt(now);
        rate.setVersion(rate.getVersion() + 1);
        entityManagerProvider.getEntityManager().merge(rate);
    }

    @Override
    public void delete(String assemblyNumber, String unitNumber) {
        throw new RuntimeException("Didn't implement yet!!!");
    }

    @Override
    public Collection<RateDto> getRates(String assembly) {
        return List.of();
    }

    private void findAndSetUnit(String number, Consumer<Unit> consumer, String msg) {
        Optional
            .ofNullable(entityManagerProvider.getEntityManager().find(Unit.class, number))
            .ifPresentOrElse(consumer, () -> throwEx(msg));
    }

    private void throwEx(String msg) {
        throw new RateFindUnitException(msg);
    }
}
