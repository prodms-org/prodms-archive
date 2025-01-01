package com.hydroyura.prodms.archive.server.service.compositor.service;

import com.hydroyura.prodms.archive.client.model.enums.UnitStatus;
import com.hydroyura.prodms.archive.client.model.enums.UnitType;
import com.hydroyura.prodms.archive.client.model.res.GetAssemblyRes;
import com.hydroyura.prodms.archive.client.model.res.GetAssemblyRes.SimpleUnit;
import com.hydroyura.prodms.archive.server.db.EntityManagerProvider;
import com.hydroyura.prodms.archive.server.db.repository.RateRepository;
import com.hydroyura.prodms.archive.server.dto.RateDto;
import com.hydroyura.prodms.archive.server.service.compositor.model.AssemblyElement;
import com.hydroyura.prodms.archive.server.service.compositor.model.Element;
import com.hydroyura.prodms.archive.server.service.compositor.model.SimpleElement;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RateCompositorService {

    private final RateRepository rateRepository;
    private final EntityManagerProvider entityManagerProvider;


    public GetAssemblyRes getRootAssembly(GetAssemblyRes res) {
        return convert(buildAssembly(res.getNumber(), res.getRates()));
    }

    private AssemblyElement buildAssembly(String number, Map<UnitType, Map<SimpleUnit, Integer>> rates) {
        var root = new AssemblyElement(number);
        rates.entrySet()
            .stream()
            .map(arg1 ->
                arg1.getValue().entrySet()
                    .stream()
                    .map(arg2 -> convertSimpleUnitToElement(arg2.getKey(), arg2.getValue(), arg1.getKey()))
                    .toList())
            .flatMap(List::stream)
            .forEach(root::addElement);
        return root;
    }

    // remove type ????
    private Map.Entry<Element, Integer> convertSimpleUnitToElement(SimpleUnit unit, Integer count, UnitType type) {
        Element element = switch (unit.getType()) {
            case ASSEMBLY -> buildAssembly(unit.getNumber(), convertRates(rateRepository.getRates(unit.getNumber())));
            default -> new SimpleElement(unit.getNumber(), type);
        };
        return Map.entry(element, count);
    }


    private GetAssemblyRes populateWithRates(final GetAssemblyRes res) {
        res.setRates(convertRates(rateRepository.getRates(res.getNumber())));
        return res;
    }

    // TODO: replace to converter
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

    // TODO: replace to converter
    private GetAssemblyRes.SimpleUnit convertRateDtoToSimpleUnit(RateDto dto) {
        var res =  new GetAssemblyRes.SimpleUnit();
        res.setName(dto.getName());
        res.setNumber(dto.getNumber());
        res.setType(convertType(dto.getType()));
        res.setStatus(convertStatus(dto.getStatus()));
        return res;
    }

    // TODO: replace to converter
    private GetAssemblyRes convert(AssemblyElement assembly) {
        var result = new GetAssemblyRes();

        result.setNumber(assembly.getNumber());
        result.setName("");
        result.setStatus(UnitStatus.TEST);

        Map<UnitType, Map<SimpleUnit, Integer>> rates = assembly.getSpecification().entrySet()
            .stream()
            .collect(Collectors.groupingBy(
                e -> e.getKey().getType(),
                Collectors.toMap(e -> convert(e.getKey()), Map.Entry::getValue)
            ));

        result.setRates(rates);
        return result;
    }

    // TODO: replace to converter
    private SimpleUnit convert(Element element) {
        SimpleUnit su = new SimpleUnit();
        su.setName("");
        su.setNumber(element.getNumber());
        su.setStatus(UnitStatus.TEST);
        su.setType(element.getType());
        return su;
    }
}
