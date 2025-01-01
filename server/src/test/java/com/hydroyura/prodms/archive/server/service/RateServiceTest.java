package com.hydroyura.prodms.archive.server.service;

import static com.hydroyura.prodms.archive.server.service.ServiceTestUtils.ASSEMBLY_NUMBER_1;
import static com.hydroyura.prodms.archive.server.service.ServiceTestUtils.UNIT_NAME_1;
import static com.hydroyura.prodms.archive.server.service.ServiceTestUtils.UNIT_NAME_2;
import static com.hydroyura.prodms.archive.server.service.ServiceTestUtils.UNIT_NUMBER_1;
import static com.hydroyura.prodms.archive.server.service.ServiceTestUtils.UNIT_NUMBER_2;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.hydroyura.prodms.archive.client.model.enums.UnitType;
import com.hydroyura.prodms.archive.server.db.EntityManagerProvider;
import com.hydroyura.prodms.archive.server.db.entity.Unit;
import com.hydroyura.prodms.archive.server.db.repository.RateRepository;
import com.hydroyura.prodms.archive.server.db.repository.UnitRepository;
import com.hydroyura.prodms.archive.server.dto.RateDto;
import jakarta.persistence.EntityTransaction;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RateServiceTest {

    private final RateService rateService;
    private final RateRepository rateRepository;
    private final UnitRepository unitRepository;
    private final EntityManagerProvider entityManagerProvider;


    RateServiceTest() {
        this.rateRepository = Mockito.mock(RateRepository.class);
        this.unitRepository = Mockito.mock(UnitRepository.class);
        this.entityManagerProvider = Mockito.mock(EntityManagerProvider.class);
        this.rateService = new RateService(rateRepository, unitRepository, entityManagerProvider, null);
    }


    @Test
    void getAssembly_AssemblyNotExist() {
        // given
        var transaction = Mockito.mock(EntityTransaction.class);
        Mockito.when(entityManagerProvider.getTransaction()).thenReturn(transaction);
        Mockito.when(unitRepository.get(ASSEMBLY_NUMBER_1)).thenReturn(Optional.empty());

        // when
        var result = rateService.getAssembly(ASSEMBLY_NUMBER_1);

        // then
        assertTrue(result.isEmpty());
    }

    @Test
    void getAssembly_AssemblyNumberNotBelongAssembly() {
        // given
        var transaction = Mockito.mock(EntityTransaction.class);
        Mockito.when(entityManagerProvider.getTransaction()).thenReturn(transaction);
        var unit = new Unit();
        unit.setType(UnitType.PART.getCode());
        Mockito.when(unitRepository.get(ASSEMBLY_NUMBER_1)).thenReturn(Optional.of(unit));

        // when
        var result = rateService.getAssembly(ASSEMBLY_NUMBER_1);

        // then
        assertTrue(result.isEmpty());
    }

    @Test
    void getAssembly_Ok() {
        // given
        var transaction = Mockito.mock(EntityTransaction.class);
        Mockito.when(entityManagerProvider.getTransaction()).thenReturn(transaction);
        var unit = new Unit();
        unit.setNumber(ASSEMBLY_NUMBER_1);
        unit.setType(UnitType.ASSEMBLY.getCode());
        Mockito.when(unitRepository.get(ASSEMBLY_NUMBER_1)).thenReturn(Optional.of(unit));
        Collection<RateDto> rates = createRates();
        Mockito.when(rateRepository.getRates(ASSEMBLY_NUMBER_1)).thenReturn(rates);
        // when
        var result = rateService.getAssembly(ASSEMBLY_NUMBER_1);

        // then
        assertTrue(result.get().getRates().get(UnitType.PART).size() == 2
            && result.get().getNumber().equals(ASSEMBLY_NUMBER_1)
        );
    }

    private Collection<RateDto> createRates() {
        RateDto rate1 = new RateDto();
        rate1.setType(1);
        rate1.setStatus(1);
        rate1.setCount(1);
        rate1.setName(UNIT_NAME_1);
        rate1.setNumber(UNIT_NUMBER_1);

        RateDto rate2 = new RateDto();
        rate2.setType(1);
        rate2.setStatus(2);
        rate2.setCount(3);
        rate2.setName(UNIT_NAME_2);
        rate2.setNumber(UNIT_NUMBER_2);

        return List.of(rate1, rate2);
    }

}