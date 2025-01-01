package com.hydroyura.prodms.archive.server.service.compositor.model;

import com.hydroyura.prodms.archive.client.model.enums.UnitType;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AssemblyElement implements Element {

    private final String number;
    private final UnitType type = UnitType.ASSEMBLY;
    private final Map<Element, Integer> specification = new HashMap<>();

    @Override
    public Map<Element, Integer> getSpecification() {
        return specification.entrySet()
            .stream()
            .flatMap(arg1 -> arg1.getKey().getSpecification().entrySet()
                .stream()
                .map(arg2 -> Map.entry(arg2.getKey(), arg1.getValue() * arg2.getValue())))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Integer::sum));
    }

    public AssemblyElement addElement(Element element, Integer count) {
        specification.put(element, count);
        return this;
    }

    public AssemblyElement addElement(Map.Entry<Element, Integer> entry) {
        addElement(entry.getKey(), entry.getValue());
        return this;
    }

    @Override
    public String getNumber() {
        return number;
    }

    @Override
    public UnitType getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AssemblyElement that = (AssemblyElement) o;
        return Objects.equals(number, that.number) && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, type);
    }
}
