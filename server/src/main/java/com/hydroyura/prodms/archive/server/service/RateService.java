package com.hydroyura.prodms.archive.server.service;

import com.hydroyura.prodms.archive.client.model.enums.UnitStatus;
import com.hydroyura.prodms.archive.client.model.enums.UnitType;
import com.hydroyura.prodms.archive.client.model.res.GetAssemblyRes;
import com.hydroyura.prodms.archive.server.db.EntityManagerProvider;
import com.hydroyura.prodms.archive.server.db.entity.Unit;
import com.hydroyura.prodms.archive.server.db.repository.RateRepository;
import com.hydroyura.prodms.archive.server.db.repository.UnitRepository;
import com.hydroyura.prodms.archive.server.dto.RateDto;
import com.hydroyura.prodms.archive.server.service.compositor.service.RateCompositorService;
import jakarta.persistence.EntityTransaction;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public final class RateService {

    private final RateRepository rateRepository;
    private final UnitRepository unitRepository;
    private final EntityManagerProvider entityManagerProvider;
    private final RateCompositorService rateCompositorService;

    public void create(String assemblyNumber, String unitNumber, Integer count) {
        EntityTransaction transaction = entityManagerProvider.getTransaction();
        try {
            transaction.begin();
            rateRepository.create(assemblyNumber, unitNumber, count);
            // TODO: add to history
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            //TODO: create custom ex
            throw new RuntimeException();
        }
    }

    public void patchCount(String assemblyNumber, String unitNumber, Integer newCount) {
        EntityTransaction transaction = entityManagerProvider.getTransaction();
        try {
            transaction.begin();
            rateRepository.create(assemblyNumber, unitNumber, newCount);
            // TODO: add to history
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            //TODO: create custom ex
            throw new RuntimeException();
        }
    }

    public void delete(String assemblyNumber, String unitNumber) {
        throw new RuntimeException("Not implemented yet!!!");
    }

    public Optional<GetAssemblyRes> getAssembly(String assemblyNumber) {
        EntityTransaction transaction = entityManagerProvider.getTransaction();
        try {
            transaction.begin();
            var result = unitRepository.get(assemblyNumber)
                .filter(u -> u.getType().equals(UnitType.ASSEMBLY.getCode()))
                .map(this::buildGetAssemblyRes)
                .map(this::populateWithRates);
            transaction.commit();
            return result;
        } catch (Exception e) {
            transaction.rollback();
            //TODO: create custom ex
            throw new RuntimeException();
        }
    }

    public Optional<GetAssemblyRes> getAssemblyExtended(String assemblyNumber) {
        return getAssembly(assemblyNumber).map(rateCompositorService::getRootAssembly);
    }

    private GetAssemblyRes buildGetAssemblyRes(Unit unit) {
        var assembly = new GetAssemblyRes();
        assembly.setName(unit.getName());
        assembly.setNumber(unit.getNumber());
        // TODO: don't forget it
        assembly.setStatus(UnitStatus.TEST);
        return assembly;
    }

    private GetAssemblyRes populateWithRates(final GetAssemblyRes res) {
        res.setRates(convertRates(rateRepository.getRates(res.getNumber())));
        return res;
    }

    // TODO: Replace to converter
    private Map<UnitType, Map<GetAssemblyRes.SimpleUnit, Integer>> convertRates(Collection<RateDto> rates) {
        return rates
            .stream()
            .collect(Collectors.groupingBy(
                dto -> this.convertType(dto.getType()),
                Collectors.toMap(this::convertRateDtoToSimpleUnit, RateDto::getCount))
        );
    }


    // TODO: remove dublication
    private UnitType convertType(Integer code) {
        // TODO: create custom ex
        return Arrays.stream(UnitType.values())
            .filter(value -> value.getCode().equals(code))
            .findFirst()
            .orElseThrow(() -> new RuntimeException(""));
    }

    // TODO: remove dublication
    private UnitStatus convertStatus(Integer code) {
        // TODO: create custom ex
        return Arrays.stream(UnitStatus.values())
            .filter(value -> value.getCode().equals(code))
            .findFirst()
            .orElseThrow(() -> new RuntimeException(""));
    }

    private GetAssemblyRes.SimpleUnit convertRateDtoToSimpleUnit(RateDto dto) {
        var res =  new GetAssemblyRes.SimpleUnit();
        res.setName(dto.getName());
        res.setNumber(dto.getNumber());
        res.setType(convertType(dto.getType()));
        res.setStatus(convertStatus(dto.getStatus()));
        return res;
    }



}
