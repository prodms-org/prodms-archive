package com.hydroyura.prodms.archive.server.validation;

import com.hydroyura.prodms.archive.server.props.ValidationProps;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@RequiredArgsConstructor
public abstract class AbstractValidator<T> implements Validator {

    private static final String VALIDATION_DISABLED_MSG = "Validation is disabled for object type = [{}]";

    protected final Logger log = LoggerFactory.getLogger(this.getClass());
    @Getter
    protected final Class<T> type;
    protected final ValidationProps props;


    @Override
    public boolean supports(Class<?> clazz) {
        return type.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (!props.getEnabled()) {
            log.debug(VALIDATION_DISABLED_MSG, this.getClass());
            return;
        }
        validateInternal(type.cast(target), errors);
    }

    protected abstract void validateInternal(T target, Errors errors);
}
