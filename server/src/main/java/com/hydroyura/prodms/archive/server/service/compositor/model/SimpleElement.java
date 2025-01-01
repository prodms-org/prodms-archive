package com.hydroyura.prodms.archive.server.service.compositor.model;

import com.hydroyura.prodms.archive.client.model.enums.UnitType;
import java.util.Map;
import java.util.Objects;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SimpleElement implements Element {

    private final String number;
    private final UnitType type;
    private final Map<Element, Integer> specification = Map.of(this, 1);

    @Override
    public Map<Element, Integer> getSpecification() {
        return specification;
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
        SimpleElement that = (SimpleElement) o;
        return Objects.equals(number, that.number) && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, type);
    }
}
