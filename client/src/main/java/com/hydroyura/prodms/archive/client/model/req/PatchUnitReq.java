package com.hydroyura.prodms.archive.client.model.req;

import lombok.Data;

@Data
public class PatchUnitReq {

    private String name;
    private Integer status;
    private String additional;

}
