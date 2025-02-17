package com.hydroyura.prodms.archive.client.model.req;


import java.util.Collections;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Collection;
import lombok.NoArgsConstructor;

@Data
public class ListUnitsReq {

    private String nameLike;
    private String numberLike;

    private Collection<String> statusIn = Collections.emptyList();
    private Collection<String> typeIn = Collections.emptyList();

    private Long createdAtStart;
    private Long createdAtEnd;

    private Long updatedAtStart;
    private Long updatedAtEnd;

    private Integer pageNum = 0;
    private Integer itemsPerPage = 20;

    private Integer sortCode = 0;

}