package com.hydroyura.prodms.archive.server.config;

import com.hydroyura.prodms.archive.server.mapper.CreateUnitReqToUnitMapper;
import com.hydroyura.prodms.archive.server.mapper.CreateUnitReqToUnitMapperImpl;
import com.hydroyura.prodms.archive.server.mapper.UnitToGetUnitResMapper;
import com.hydroyura.prodms.archive.server.mapper.UnitToGetUnitResMapperImpl;
import com.hydroyura.prodms.archive.server.mapper.UnitToUnitHistMapper;
import com.hydroyura.prodms.archive.server.mapper.UnitToUnitHistMapperImpl;
import com.hydroyura.prodms.archive.server.mapper.UnitToUnitResMapper;
import com.hydroyura.prodms.archive.server.mapper.UnitToUnitResMapperImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MappersConfig {

    @Bean
    CreateUnitReqToUnitMapper createUnitReqToUnitMapper() {
        return new CreateUnitReqToUnitMapperImpl();
    }

    @Bean
    UnitToUnitHistMapper unitToUnitHistMapper() {
        return new UnitToUnitHistMapperImpl();
    }

    @Bean
    UnitToGetUnitResMapper unitToUnitDtoMapper() {
        return new UnitToGetUnitResMapperImpl();
    }

    @Bean
    UnitToUnitResMapper unitToUnitDtoMapper2() {
        return new UnitToUnitResMapperImpl();
    }

}
