package com.hydroyura.prodms.archive.server.validation;

import static com.hydroyura.prodms.archive.server.validation.ValidationManagerUtils.AGE;
import static com.hydroyura.prodms.archive.server.validation.ValidationManagerUtils.ERROR_CODE_AGE_GREATER;

import com.hydroyura.prodms.archive.server.props.ValidationProps;
import org.springframework.validation.Errors;

public class TestValidator extends AbstractValidator<TestObjectForValidation> {

    public TestValidator(ValidationProps props) {
        super(TestObjectForValidation.class, props);
    }

    @Override
    protected void validateInternal(TestObjectForValidation target, Errors errors) {
        if (target.getAge() == null || target.getAge() < AGE) {
            errors.reject(ERROR_CODE_AGE_GREATER);
        }
    }
}