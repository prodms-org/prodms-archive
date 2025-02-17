package com.hydroyura.prodms.archive.server.service.compositor.service;

import static com.hydroyura.prodms.archive.server.service.ServiceTestUtils.ASSEMBLY_NAME_1;
import static com.hydroyura.prodms.archive.server.service.ServiceTestUtils.ASSEMBLY_NUMBER_1;
import static com.hydroyura.prodms.archive.server.service.ServiceTestUtils.ASSEMBLY_NUMBER_2;
import static com.hydroyura.prodms.archive.server.service.ServiceTestUtils.PART_NUMBER_1;
import static com.hydroyura.prodms.archive.server.service.ServiceTestUtils.PART_NUMBER_2;
import static com.hydroyura.prodms.archive.server.service.ServiceTestUtils.PART_NUMBER_3;
import static com.hydroyura.prodms.archive.server.service.ServiceTestUtils.PART_NUMBER_4;
import static com.hydroyura.prodms.archive.server.service.ServiceTestUtils.STANDARD_NUMBER_1;
import static com.hydroyura.prodms.archive.server.service.ServiceTestUtils.TEST_NAME;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.hydroyura.prodms.archive.client.model.enums.UnitStatus;
import com.hydroyura.prodms.archive.client.model.enums.UnitType;
import com.hydroyura.prodms.archive.client.model.res.GetAssemblyRes;
import com.hydroyura.prodms.archive.client.model.res.GetAssemblyRes.SimpleUnit;
import com.hydroyura.prodms.archive.server.db.EntityManagerProvider;
import com.hydroyura.prodms.archive.server.db.repository.RateRepository;
import com.hydroyura.prodms.archive.server.dto.RateDto;
import com.hydroyura.prodms.archive.server.service.compositor.model.Element;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RateCompositorServiceTest {

    private final RateRepository rateRepository;
    private final RateCompositorService rateCompositorService;


    RateCompositorServiceTest() {
        EntityManagerProvider entityManagerProvider = Mockito.mock(EntityManagerProvider.class);
        this.rateRepository = Mockito.mock(RateRepository.class);
        this.rateCompositorService = new RateCompositorService(rateRepository, entityManagerProvider);
    }


//    @Test
//    void getAssembly_rowsWithoutSubAssemblies() {
//        // given
//        GetAssemblyRes res = new GetAssemblyRes();
//        res.setStatus(UnitStatus.TEST);
//        res.setNumber(ASSEMBLY_NUMBER_1);
//        res.setName(ASSEMBLY_NAME_1);
//
//        Map<UnitType, Map<SimpleUnit, Integer>> rates = new HashMap<>();
//        Map<GetAssemblyRes.SimpleUnit, Integer> parts = new HashMap<>();
//        Map<GetAssemblyRes.SimpleUnit, Integer> standards = new HashMap<>();
//
//        rates.put(UnitType.PART, parts);
//        parts.put(createSimpleUnit(UnitType.PART, PART_NUMBER_1), 1);
//        parts.put(createSimpleUnit(UnitType.PART, PART_NUMBER_2), 2);
//        parts.put(createSimpleUnit(UnitType.PART, PART_NUMBER_3), 3);
//
//        rates.put(UnitType.STANDARD, standards);
//        standards.put(createSimpleUnit(UnitType.STANDARD, STANDARD_NUMBER_1), 1);
//
//        res.setRates(rates);
//
//        // when
//        var result = rateCompositorService.getRootAssembly(res).getRates().values()
//            .stream()
//            .flatMap(e -> e.entrySet().stream())
//            .collect(Collectors.toMap(e -> e.getKey().getNumber(), Map.Entry::getValue));
//
//        // then
//        assertTrue(result.size() == 4
//            && result.keySet().stream().anyMatch(PART_NUMBER_1::equals)
//            && result.keySet().stream().anyMatch(PART_NUMBER_2::equals)
//            && result.keySet().stream().anyMatch(PART_NUMBER_3::equals)
//            && result.keySet().stream().anyMatch(STANDARD_NUMBER_1::equals)
//        );
//    }
//
//    //@Test
//    void getAssembly_rowsWithFirstLevelSubAssemblies() {
//        // given
//        GetAssemblyRes res = new GetAssemblyRes();
//        res.setStatus(UnitStatus.TEST);
//        res.setNumber(ASSEMBLY_NUMBER_1);
//        res.setName(ASSEMBLY_NAME_1);
//
//        Map<UnitType, Map<SimpleUnit, Integer>> rates = new HashMap<>();
//        Map<GetAssemblyRes.SimpleUnit, Integer> parts = new HashMap<>();
//        Map<GetAssemblyRes.SimpleUnit, Integer> standards = new HashMap<>();
//        Map<GetAssemblyRes.SimpleUnit, Integer> assemblies = new HashMap<>();
//
//        rates.put(UnitType.PART, parts);
//        parts.put(createSimpleUnit(UnitType.PART, PART_NUMBER_1), 1);
//        parts.put(createSimpleUnit(UnitType.PART, PART_NUMBER_2), 2);
//        parts.put(createSimpleUnit(UnitType.PART, PART_NUMBER_3), 3);
//
//        rates.put(UnitType.STANDARD, standards);
//        standards.put(createSimpleUnit(UnitType.STANDARD, STANDARD_NUMBER_1), 1);
//
//        rates.put(UnitType.ASSEMBLY, assemblies);
//        assemblies.put(createSimpleUnitAssembly(ASSEMBLY_NUMBER_2), 3);
//
//        res.setRates(rates);
//
//        Mockito.when(rateRepository.getRates(ASSEMBLY_NUMBER_2)).thenReturn(assembly2Rates());
//        // when
//        Map<String, Integer> result = rateCompositorService.getRootAssembly(res).getRates().values()
//            .stream()
//            .flatMap(e -> e.entrySet().stream())
//            .collect(Collectors.toMap(e -> e.getKey().getNumber(), Map.Entry::getValue));
//
//        // then
//        assertTrue(result.size() == 5
//            && result.get(PART_NUMBER_1) == 1
//            && result.get(PART_NUMBER_2) == 20
//            && result.get(PART_NUMBER_3) == 3
//            && result.get(PART_NUMBER_4) == 3
//            && result.get(STANDARD_NUMBER_1) == 7
//        );
//    }

    private SimpleUnit createSimpleUnit(UnitType type, String number) {
        if (type.equals(UnitType.ASSEMBLY)) {
            throw new RuntimeException("Don't use [createSimpleUnit] for assembly type");
        }
        var simpleUnit = new SimpleUnit();
        simpleUnit.setStatus(UnitStatus.TEST);
        simpleUnit.setType(type);
        simpleUnit.setNumber(number);
        simpleUnit.setName(TEST_NAME);
        return simpleUnit;
    }

    private SimpleUnit createSimpleUnitAssembly(String number) {
        var simpleUnit = new SimpleUnit();
        simpleUnit.setStatus(UnitStatus.TEST);
        simpleUnit.setType(UnitType.ASSEMBLY);
        simpleUnit.setNumber(number);
        simpleUnit.setName(TEST_NAME);
        return simpleUnit;
    }

    private Collection<RateDto> assembly2Rates() {
        Collection<RateDto> rates = new ArrayList<>();

        RateDto partNum2 = new RateDto();
        partNum2.setStatus(1);
        partNum2.setCount(6);
        partNum2.setNumber(PART_NUMBER_2);
        partNum2.setName(TEST_NAME);
        partNum2.setType(1);
        rates.add(partNum2);

        RateDto partNum4 = new RateDto();
        partNum4.setStatus(1);
        partNum4.setCount(1);
        partNum4.setNumber(PART_NUMBER_4);
        partNum4.setName(TEST_NAME);
        partNum4.setType(1);
        rates.add(partNum4);

        RateDto stNum1 = new RateDto();
        stNum1.setStatus(1);
        stNum1.setCount(2);
        stNum1.setNumber(STANDARD_NUMBER_1);
        stNum1.setName(TEST_NAME);
        stNum1.setType(4);
        rates.add(stNum1);

        return rates;
    }
}