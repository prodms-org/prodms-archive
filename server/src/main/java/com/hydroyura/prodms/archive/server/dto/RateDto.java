package com.hydroyura.prodms.archive.server.dto;

import lombok.Data;

@Data
public class RateDto {

    private Integer type;
    private Integer count;
    private String number;
    private String name;
    private Integer status;

}
