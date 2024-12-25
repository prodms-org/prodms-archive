package com.hydroyura.prodms.archive.server.service;

import com.hydroyura.prodms.archive.server.db.repository.RateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RateService {

    private final RateRepository rateRepository;


    public void create(String assemblyNumber, String unitNumber, Integer count) {

    }


}
