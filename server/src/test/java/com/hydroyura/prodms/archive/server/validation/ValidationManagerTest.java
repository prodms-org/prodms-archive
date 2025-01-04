package com.hydroyura.prodms.archive.server.validation;

import com.hydroyura.prodms.archive.server.exception.model.ValidationException;
import org.junit.jupiter.api.Test;
import org.springframework.validation.Validator;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.hydroyura.prodms.archive.server.validation.ValidationManagerUtils.*;
import static org.junit.jupiter.api.Assertions.*;


class ValidationManagerTest {

    //private final ValidationManager validationManager;

//    ValidationManagerTest() {
//        var validator = Validator.forType(TestObjectForValidation.class, (obj, errs) -> {
//            if (obj.getAge() ==  null || obj.getAge() < AGE) {
//                errs.reject(ERROR_CODE_AGE_GREATER);
//            }
//        });
//
//        Map<Class<?>, List<AbstractValidator<?>>> validators = Map.of(TestObjectForValidation.class, List.of(validator));
//        this.validationManager = new ValidationManager(validators, null);
//    }
//
//
//    @Test
//    void validationSuccessTest() {
//        var target = new TestObjectForValidation();
//        target.setAge(AGE + 10);
//        target.setName(NAME);
//        assertDoesNotThrow(() -> validationManager.validate(target, TestObjectForValidation.class));
//    }
//
//    @Test
//    void validationFailedTest() {
//        var target = new TestObjectForValidation();
//        target.setAge(AGE - 5);
//        target.setName(NAME);
//        assertThrows(ValidationException.class, () -> validationManager.validate(target, TestObjectForValidation.class));
//    }
//
//    @Test
//    void notFoundValidatorsTest() {
//        var target = Collections.EMPTY_MAP;
//        //TODO: fix this test, e.g. skyMock method call
//        //assertThrows(RuntimeException.class, () -> validationManager.validate(target, Map.class));
//    }
}