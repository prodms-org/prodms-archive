package com.hydroyura.prodms.archive.server.batch;

import com.hydroyura.prodms.archive.client.model.req.CreateUnitReq;
import com.hydroyura.prodms.archive.server.service.UnitService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

@Slf4j
@RequiredArgsConstructor
public class UnitWriter implements ItemWriter<CreateUnitReq> {

    private final UnitService unitService;

    @Override
    public void write(Chunk<? extends CreateUnitReq> chunk) throws Exception {
        unitService.create((List<CreateUnitReq>) chunk.getItems());
    }
}
