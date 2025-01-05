package com.hydroyura.prodms.archive.server.validation.validators;

import static com.hydroyura.prodms.archive.server.SharedConstants.LOG_MSG_VALIDATION_NUMBER_BEGIN;
import static com.hydroyura.prodms.archive.server.validation.ValidationMessageCodes.CREATE_UNIT_NUMBER_EMPTY;
import static com.hydroyura.prodms.archive.server.validation.ValidationMessageCodes.DEFAULT_MSG;
import static com.hydroyura.prodms.archive.server.validation.ValidationMessageCodes.NUMBER_UNIT_REGEX;

import com.hydroyura.prodms.archive.server.props.ValidationProps;
import com.hydroyura.prodms.archive.server.validation.AbstractValidator;
import com.hydroyura.prodms.archive.server.validation.enums.NumberKey;
import com.hydroyura.prodms.archive.server.validation.model.WrapNumber;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class WrapNumberValidator extends AbstractValidator<WrapNumber> {

    private static final String NUMBER_KEY = "number";

    public WrapNumberValidator(ValidationProps props) {
        super(WrapNumber.class, props);
    }

    @Override
    protected void validateInternal(WrapNumber target, Errors errors) {
        validateUnitNumber(target, errors);
    }

    private void validateUnitNumber(WrapNumber target, Errors errors) {
        if (target.getType().equals(String.class) && target.getKey().equals(NumberKey.UNIT)) {
            log.info(LOG_MSG_VALIDATION_NUMBER_BEGIN, target.getNumber(), target.getKey());
            String number = (String) target.getNumber();
            String pattern = props.getNumber().getUnitRegex();
            if (!number.matches(pattern)) {
                errors.rejectValue(NUMBER_KEY, NUMBER_UNIT_REGEX, DEFAULT_MSG);
            }
        }
    }
}
