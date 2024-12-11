package com.hydroyura.prodms.archive.server.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "filters.defaults")
public class DefaultFiltersProps {


    @Data
    public static class ListUnit {
        private Integer pageNum;
        private Integer itemsPerPage;
        private Integer sortCode;
    }
}
