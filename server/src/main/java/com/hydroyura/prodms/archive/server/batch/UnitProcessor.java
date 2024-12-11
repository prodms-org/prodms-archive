package com.hydroyura.prodms.archive.server.batch;

import com.hydroyura.prodms.archive.client.model.req.CreateUnitReq;
import com.hydroyura.prodms.archive.server.validation.ValidationManager;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;

@RequiredArgsConstructor
public class UnitProcessor implements ItemProcessor<CreateUnitReq, CreateUnitReq> {


    private final ValidationManager validationManager;

    @Override
    public CreateUnitReq process(CreateUnitReq item) throws Exception {
        validationManager.validate(item, CreateUnitReq.class);
        return item;
    }

}
