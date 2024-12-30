package com.hydroyura.prodms.archive.server.service.compositor.service;

import com.hydroyura.prodms.archive.client.model.enums.UnitType;
import com.hydroyura.prodms.archive.client.model.res.GetAssemblyRes;
import com.hydroyura.prodms.archive.client.model.res.GetAssemblyRes.SimpleUnit;
import com.hydroyura.prodms.archive.server.service.compositor.model.AssemblyElement;
import com.hydroyura.prodms.archive.server.service.compositor.model.Element;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class RateCompositorService {

    public AssemblyElement getExtendedAssembly(GetAssemblyRes res) {
        var root = convert(res);
        return root;
    }

    private AssemblyElement convert(GetAssemblyRes res) {
        var root = new AssemblyElement(res.getNumber());

        res.getRates().entrySet()
            .stream()
            .map(arg1 ->
                arg1.getValue().entrySet()
                    .stream()
                    .map(arg2 -> convert(arg2.getKey(), arg2.getValue(), arg1.getKey()))
                    .toList())
            .flatMap(List::stream)
            .forEach(root::addElement);

        return root;
    }

    private Map.Entry<Element, Integer> convert(SimpleUnit unit, Integer count, UnitType type) {

        return null;
    }



}
