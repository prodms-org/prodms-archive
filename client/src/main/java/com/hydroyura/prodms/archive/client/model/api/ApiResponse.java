package com.hydroyura.prodms.archive.client.model.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;

import java.sql.Timestamp;
import java.util.UUID;

@Data
public class ApiResponse<T> {

    private UUID id;
    private Timestamp timestamp;
    private T data;
    private Collection<String> warnings = new ArrayList<>();
    private Collection<String> errors = new ArrayList<>();

}
