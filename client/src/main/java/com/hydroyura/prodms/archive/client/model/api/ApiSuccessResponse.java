package com.hydroyura.prodms.archive.client.model.api;

import lombok.Data;

import java.sql.Timestamp;
import java.util.UUID;

@Data
public class ApiSuccessResponse<T> {

    private UUID id;
    private Timestamp timestamp;
    private T data;

}
