package com.hydroyura.prodms.archive.server.validation.validators;

import static com.hydroyura.prodms.archive.server.validation.ValidationMessageCodes.CREATE_UNIT_NAME_REGEX;
import static com.hydroyura.prodms.archive.server.validation.ValidationMessageCodes.CREATE_UNIT_NUMBER_EMPTY;
import static com.hydroyura.prodms.archive.server.validation.ValidationMessageCodes.CREATE_UNIT_NUMBER_REGEX;
import static com.hydroyura.prodms.archive.server.validation.ValidationMessageCodes.CREATE_UNIT_STATUS_EMPTY;
import static com.hydroyura.prodms.archive.server.validation.ValidationMessageCodes.CREATE_UNIT_TYPE_EMPTY;
import static org.junit.jupiter.api.Assertions.*;

import com.hydroyura.prodms.archive.client.model.enums.UnitStatus;
import com.hydroyura.prodms.archive.client.model.enums.UnitType;
import com.hydroyura.prodms.archive.client.model.req.CreateUnitReq;
import com.hydroyura.prodms.archive.server.props.ValidationProps;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.Errors;
import org.springframework.validation.SimpleErrors;

class CreateUnitReqValidatorTest {

    private static final String ONLY_UPPER_LETTERS = "^[A-Z]+$";
    private static final String ONLY_LOWER_LETTERS = "^[a-z]+$";

    private final CreateUnitReqValidator validator;

    CreateUnitReqValidatorTest() {
        var prop = new ValidationProps();
        prop.setEnabled(Boolean.TRUE);
        ValidationProps.CreateUnit createUnitProps = new ValidationProps.CreateUnit();
        createUnitProps.setNumberRegex(ONLY_UPPER_LETTERS);
        createUnitProps.setNameRegex(ONLY_LOWER_LETTERS);
        prop.setCreateUnit(createUnitProps);
        this.validator = new CreateUnitReqValidator(prop);
    }

    @Test
    void validTarget() {
        var unit = buildTarget("name", "NUMBER", UnitType.PART, UnitStatus.TEST);
        Errors errors = new SimpleErrors(unit);
        validator.validate(unit, errors);
        assertFalse(errors.hasErrors());
    }

    @Test
    void invalidTarget() {
        var unit = buildTarget("NAME", "", null, null);
        Errors errors = new SimpleErrors(unit);
        validator.validate(unit, errors);
        assertTrue(errors.hasErrors());
        Collection<String> errorCodes = errors.getAllErrors()
            .stream()
            .map(DefaultMessageSourceResolvable::getCodes)
            .filter(Objects::nonNull)
            .flatMap(Arrays::stream)
            .collect(Collectors.toCollection(ArrayList::new));
        assertTrue(
            errorCodes.contains(CREATE_UNIT_NAME_REGEX) &&
                errorCodes.contains(CREATE_UNIT_NUMBER_EMPTY) &&
                errorCodes.contains(CREATE_UNIT_TYPE_EMPTY) &&
                errorCodes.contains(CREATE_UNIT_STATUS_EMPTY)
        );
    }

    private CreateUnitReq buildTarget(String name, String number, UnitType type, UnitStatus status) {
        var target = new CreateUnitReq();
        target.setName(name);
        target.setNumber(number);
        target.setStatus(status);
        target.setType(type);
        return target;
    }

}