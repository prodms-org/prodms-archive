package com.hydroyura.prodms.archive.server.validation;

import com.hydroyura.prodms.archive.server.exception.model.ValidationException;
import com.hydroyura.prodms.archive.server.props.ValidationProps;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.hydroyura.prodms.archive.server.validation.ValidationManagerUtils.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ValidationManagerTest {

    private final ValidationManager validationManager;

    ValidationManagerTest() {
        var props = new ValidationProps();
        props.setEnabled(Boolean.TRUE);
        Map<Class<?>, List<AbstractValidator<?>>> validators = Map.of(
            TestObjectForValidation.class, List.of(new TestValidator(props))
        );
        this.validationManager = Mockito.spy(new ValidationManager(validators));
    }


    @Test
    void validationSuccessTest() {
        var target = new TestObjectForValidation();
        target.setAge(AGE + 10);
        target.setName(NAME);
        assertDoesNotThrow(() -> validationManager.validate(target, TestObjectForValidation.class));
    }

    @Test
    void validationFailedTest() {
        var target = new TestObjectForValidation();
        target.setAge(AGE - 5);
        target.setName(NAME);
        assertThrows(ValidationException.class, () -> validationManager.validate(target, TestObjectForValidation.class));
    }

    @Test
    void notFoundValidatorsTest() {
        // TODO: create test for missing validator for certain types
    }

}