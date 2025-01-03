package com.hydroyura.prodms.archive.server.validation;

import com.hydroyura.prodms.archive.server.exception.model.ValidationException;
import com.hydroyura.prodms.archive.server.props.ValidationProps;
import jakarta.annotation.PostConstruct;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.SimpleErrors;
import org.springframework.validation.Validator;

@Slf4j
@Component
@RequiredArgsConstructor
public class ValidationManager {

    private static final String INVALID_MSG = "Validation failed for object with type = [%s] and data = [%s]";
    private static final String NO_VALIDATORS_FOUND_MSG = "Can not find required validators for type = [{}]...";

    private final Map<Class<?>, Collection<Validator>> validators;

    private final ValidationProps props;


    public <T> void validate(T object, Class<T> type) {
        Collection<Validator> acceptableValidators = getValidatorForType(type);

        if (acceptableValidators.isEmpty()) {
            log.warn(NO_VALIDATORS_FOUND_MSG, type.getSimpleName());
        } else {
            Errors errors = new SimpleErrors(object);
            acceptableValidators.forEach(v -> v.validate(object, errors));
            if (errors.hasErrors()) {
                throw new ValidationException(errors, INVALID_MSG.formatted(type, object));
            }
        }
    }

    private Collection<Validator> getValidatorForType(Class<?> type) {
        return Optional
            .ofNullable(validators.get(type))
            .orElse(Collections.emptyList());
    }

}
