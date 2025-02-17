package com.hydroyura.prodms.archive.server.config;

import com.hydroyura.prodms.archive.server.validation.AbstractValidator;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ValidationConfig {

    @Bean
    Map<Class<?>, List<AbstractValidator<?>>> sortedValidators(Collection<AbstractValidator<?>> validators) {
        return validators
            .stream()
            .collect(Collectors.groupingBy(AbstractValidator::getType));
    }

}
