package com.hydroyura.prodms.archive.server.db.repository;

import static com.hydroyura.prodms.archive.server.SharedConstants.EX_MSG_UNIT_PATCH;
import static com.hydroyura.prodms.archive.server.SharedConstants.LOG_MSG_UNIT_NOT_FOUND;

import com.hydroyura.prodms.archive.client.model.enums.EnumUtils;
import com.hydroyura.prodms.archive.client.model.req.ListUnitsReq;
import com.hydroyura.prodms.archive.server.db.EntityManagerProvider;
import com.hydroyura.prodms.archive.server.db.entity.Unit;
import com.hydroyura.prodms.archive.server.db.order.UnitOrder;
import com.hydroyura.prodms.archive.server.exception.model.db.UnitPatchException;
import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
@Repository
@RequiredArgsConstructor
@Slf4j
public class UnitRepositoryImpl implements UnitRepository {

    private final EntityManagerProvider entityManagerProvider;

    @Override
    public void create(Unit unit) {
        entityManagerProvider.getEntityManager().persist(unit);
    }

    @Override
    public Optional<Unit> get(String number) {
        try {
            CriteriaBuilder criteriaBuilder = entityManagerProvider.getEntityManager().getCriteriaBuilder();
            CriteriaQuery<Unit> criteriaQuery = criteriaBuilder.createQuery(Unit.class);
            Root<Unit> root = criteriaQuery.from(Unit.class);
            root.fetch("history", JoinType.LEFT);
            Collection<Predicate> andPredicates = new ArrayList<>();
            andPredicates.add(criteriaBuilder.equal(root.get("number"), number));
            andPredicates.add(criteriaBuilder.equal(root.get("isActive"), Boolean.TRUE));

            criteriaQuery.where(criteriaBuilder.and(andPredicates.toArray(Predicate[]::new)));

            var unit = entityManagerProvider.getEntityManager().createQuery(criteriaQuery).getSingleResult();
            return Optional.of(unit);
        } catch (NoResultException ex) {
            log.warn(LOG_MSG_UNIT_NOT_FOUND, number);
            return Optional.empty();
        }
    }

    @Override
    public Optional<String> delete(String number) {
        return get(number)
            .map(u -> {
                u.setUpdatedAt(Instant.now().getEpochSecond());
                u.setVersion(u.getVersion() + 1);
                u.setIsActive(Boolean.FALSE);
                return u;
            })
            .map(entityManagerProvider.getEntityManager()::merge)
            .map(Unit::getNumber);
    }

    @Override
    public Collection<Unit> list(ListUnitsReq filter) {
        CriteriaBuilder criteriaBuilder = entityManagerProvider.getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Unit> criteriaQuery = criteriaBuilder.createQuery(Unit.class);
        Root<Unit> root = criteriaQuery.from(Unit.class);

        Collection<Predicate> andPredicates = new ArrayList<>();
        // TODO: use field names like metadata
        andPredicates.add(criteriaBuilder.equal(root.get("isActive"), Boolean.TRUE));

        wrapField(filter::getNumberLike)
            .map(value -> "%" + value + "%")
            .map(value -> criteriaBuilder.like(root.get("number"), value))
            .ifPresent(andPredicates::add);

        wrapField(filter::getNameLike)
            .map(value -> "%" + value + "%")
            .map(value -> criteriaBuilder.like(root.get("name"), value))
            .ifPresent(andPredicates::add);

        wrapField(filter::getCreatedAtStart)
            .map(value -> criteriaBuilder.ge(root.get("createdAt"), value))
            .ifPresent(andPredicates::add);

        wrapField(filter::getCreatedAtEnd)
            .map(value -> criteriaBuilder.le(root.get("createdAt"), value))
            .ifPresent(andPredicates::add);

        wrapField(filter::getUpdatedAtStart)
            .map(value -> criteriaBuilder.ge(root.get("updatedAt"), value))
            .ifPresent(andPredicates::add);

        wrapField(filter::getUpdatedAtEnd)
            .map(value -> criteriaBuilder.le(root.get("updatedAt"), value))
            .ifPresent(andPredicates::add);

        if (!filter.getStatusIn().isEmpty()) {
            var values = filter.getStatusIn()
                .stream()
                .map(EnumUtils::getUnitStatusCodeByString)
                .toList();
            andPredicates.add(root.get("status").in(values));
        }

        if (!filter.getTypeIn().isEmpty()) {
            var values = filter.getTypeIn()
                .stream()
                .map(EnumUtils::getUnitTypeCodeByString)
                .toList();
            andPredicates.add(root.get("type").in(values));
        }

        var array = andPredicates.toArray(Predicate[]::new);
        Predicate wherePredicate = criteriaBuilder.and(array);

        criteriaQuery
            .select(root)
            .where(wherePredicate)
            .orderBy(getUnitOrderByCode(filter.getSortCode()).calcOrder(root, criteriaBuilder));

        return entityManagerProvider.getEntityManager().createQuery(criteriaQuery)
            .setFirstResult(filter.getPageNum() * filter.getItemsPerPage())
            .setMaxResults(filter.getItemsPerPage())
            .getResultList();
    }

    // TODO: make this code prettier
    @Override
    public void patch(Unit unit) {
        var currentUnit = get(unit.getNumber());
        if (currentUnit.isPresent()) {
            boolean isNameNew = currentUnit.get().getName().equals(unit.getName());
            boolean isStatusNew = currentUnit.get().getStatus().equals(unit.getStatus());
            boolean isAdditionalNew = currentUnit.get().getAdditional().equals(unit.getAdditional());

            if (!isNameNew || !isStatusNew || !isAdditionalNew) {
                Long now = Instant.now().getEpochSecond();
                Unit pachedUnit = currentUnit.get();
                pachedUnit.setUpdatedAt(now);
                pachedUnit.setVersion(pachedUnit.getVersion() + 1);
                pachedUnit.setName(unit.getName());
                pachedUnit.setStatus(unit.getStatus());
                pachedUnit.setAdditional(unit.getAdditional());
                entityManagerProvider.getEntityManager().merge(pachedUnit);
                return;
            }
        }
        throw new UnitPatchException(EX_MSG_UNIT_PATCH.formatted(unit.getNumber()));
    }


    private <R> Optional<R> wrapField(Supplier<R> supplier) {
        return Optional.ofNullable(supplier.get());
    }

    private static UnitOrder getUnitOrderByCode(Integer code) {
        // TODO: custom error
        return Arrays.stream(UnitOrder.values())
            .filter(value -> value.getCode().equals(code))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Can not find ...."));
    }

}