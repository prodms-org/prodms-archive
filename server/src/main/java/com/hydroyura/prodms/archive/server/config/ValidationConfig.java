package com.hydroyura.prodms.archive.server.config;

import com.hydroyura.prodms.archive.client.model.req.CreateUnitReq;
import com.hydroyura.prodms.archive.client.model.req.ListUnitsReq;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.Validator;

@Configuration
public class ValidationConfig {

    @Bean
    public Map<Class<?>, Collection<Validator>> groupedValidators(Collection<Validator> validators) {
        return getTypeForValidation()
            .stream()
            .collect(Collectors.toMap(
                Function.identity(),
                type -> validators.stream().filter(v -> v.supports(type)).toList())
            );
    }

    private static Collection<Class<?>> getTypeForValidation() {
        var types = new ArrayList<Class<?>>();
        types.add(CreateUnitReq.class);
        types.add(ListUnitsReq.class);
        return types;
    }
}
