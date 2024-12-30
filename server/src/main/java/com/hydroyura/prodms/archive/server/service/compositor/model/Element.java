package com.hydroyura.prodms.archive.server.service.compositor.model;

import com.hydroyura.prodms.archive.client.model.enums.UnitType;
import java.util.Map;

public interface Element {

    Map<Element, Integer> getSpecification();
    String getNumber();
    UnitType getType();

}
