package com.hydroyura.prodms.archive.server.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "validation")
public class ValidationProps {

    private Boolean enabled;
    private CreateUnit createUnit;


    @Data
    public static class CreateUnit {
        private String numberRegex;
        private String nameRegex;
    }

}
