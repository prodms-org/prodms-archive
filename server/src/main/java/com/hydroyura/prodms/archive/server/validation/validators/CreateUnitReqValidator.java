package com.hydroyura.prodms.archive.server.validation.validators;

import static com.hydroyura.prodms.archive.server.validation.ValidationMessageCodes.CREATE_UNIT_NAME_EMPTY;
import static com.hydroyura.prodms.archive.server.validation.ValidationMessageCodes.CREATE_UNIT_NAME_REGEX;
import static com.hydroyura.prodms.archive.server.validation.ValidationMessageCodes.CREATE_UNIT_NUMBER_EMPTY;
import static com.hydroyura.prodms.archive.server.validation.ValidationMessageCodes.CREATE_UNIT_NUMBER_REGEX;
import static com.hydroyura.prodms.archive.server.validation.ValidationMessageCodes.CREATE_UNIT_STATUS_EMPTY;
import static com.hydroyura.prodms.archive.server.validation.ValidationMessageCodes.CREATE_UNIT_TYPE_EMPTY;
import static com.hydroyura.prodms.archive.server.validation.ValidationMessageCodes.DEFAULT_MSG;

import com.hydroyura.prodms.archive.client.model.req.CreateUnitReq;
import com.hydroyura.prodms.archive.server.props.ValidationProps;
import com.hydroyura.prodms.archive.server.validation.AbstractValidator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

@Component
public class CreateUnitReqValidator extends AbstractValidator<CreateUnitReq> {

    private static final String NUMBER_KEY = "number";
    private static final String NAME_KEY = "name";
    private static final String TYPE_KEY = "type";
    private static final String STATUS_KEY = "status";

    public CreateUnitReqValidator(ValidationProps props) {
        super(CreateUnitReq.class, props);
    }

    @Override
    protected void validateInternal(CreateUnitReq target, Errors errors) {
        validateName(target.getName(), errors);
        validateNumber(target.getNumber(), errors);
        ValidationUtils.rejectIfEmpty(errors, TYPE_KEY, CREATE_UNIT_TYPE_EMPTY, DEFAULT_MSG);
        ValidationUtils.rejectIfEmpty(errors, STATUS_KEY, CREATE_UNIT_STATUS_EMPTY, DEFAULT_MSG);
    }

    private void validateNumber(String number, Errors errors) {
        if (StringUtils.isBlank(number)) {
            errors.rejectValue(NUMBER_KEY, CREATE_UNIT_NUMBER_EMPTY, DEFAULT_MSG);
        } else if (!number.matches(props.getCreateUnit().getNumberRegex())) {
            errors.rejectValue(NUMBER_KEY, CREATE_UNIT_NUMBER_REGEX, DEFAULT_MSG);
        }
    }

    private void validateName(String name, Errors errors) {
        if (StringUtils.isBlank(name)) {
            errors.rejectValue(NAME_KEY, CREATE_UNIT_NAME_EMPTY, DEFAULT_MSG);
        } else if (!name.matches(props.getCreateUnit().getNameRegex())) {
            errors.rejectValue(NAME_KEY, CREATE_UNIT_NAME_REGEX, DEFAULT_MSG);
        }
    }
}