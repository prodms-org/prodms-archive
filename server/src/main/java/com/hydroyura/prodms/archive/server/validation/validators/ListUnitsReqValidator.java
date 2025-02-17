package com.hydroyura.prodms.archive.server.validation.validators;

import com.hydroyura.prodms.archive.client.model.req.ListUnitsReq;
import com.hydroyura.prodms.archive.server.props.ValidationProps;
import com.hydroyura.prodms.archive.server.validation.AbstractValidator;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class ListUnitsReqValidator extends AbstractValidator<ListUnitsReq> {

    public ListUnitsReqValidator(ValidationProps props) {
        super(ListUnitsReq.class, props);
    }

    @Override
    protected void validateInternal(ListUnitsReq target, Errors errors) {
        int a = 1;
        // TODO: populate with defaults
    }
}