package com.hydroyura.prodms.archive.client.model.api;

import lombok.Data;

import java.sql.Timestamp;
import java.util.Map;
import java.util.UUID;

@Data
public class ApiErrorResponse {

    private UUID id;
    private Timestamp timestamp;
    private Map<String, String> errors;

}
