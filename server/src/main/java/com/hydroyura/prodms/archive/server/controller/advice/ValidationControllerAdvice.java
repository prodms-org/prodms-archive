package com.hydroyura.prodms.archive.server.controller.advice;

import static com.hydroyura.prodms.archive.server.SharedConstants.REQUEST_ATTR_UUID_KEY;
import static com.hydroyura.prodms.archive.server.SharedConstants.REQUEST_TIMESTAMP_KEY;
import static com.hydroyura.prodms.archive.server.SharedConstants.RESPONSE_ERROR_MSG_ENTITY_NOT_FOUND;

import com.hydroyura.prodms.archive.client.model.api.ApiRes;
import com.hydroyura.prodms.archive.server.exception.model.ValidationException;
import jakarta.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ValidationControllerAdvice {

    @ExceptionHandler(value = ValidationException.class)
    public ResponseEntity<ApiRes<Void>> validation(HttpServletRequest req, ValidationException ex) {
        var apiRes = new ApiRes<Void>();
        apiRes.setId(extractRequestUUID(req));
        apiRes.setTimestamp(extractRequestTimestamp(req));
        apiRes.getErrors().add(ex.getMessage());

        // TODO: replace codes with msg with args
        ex.getErrors().getAllErrors()
            .stream()
            .map(DefaultMessageSourceResolvable::getCodes)
            .filter(Objects::nonNull)
            .flatMap(Arrays::stream)
            .forEach(apiRes.getErrors()::add);

        return new ResponseEntity<>(apiRes, HttpStatus.BAD_REQUEST);
    }


    private static UUID extractRequestUUID(HttpServletRequest request) {
        return Optional
            .ofNullable(request.getAttribute(REQUEST_ATTR_UUID_KEY))
            .map(UUID.class::cast)
            .orElseThrow(RuntimeException::new);
    }

    private static Timestamp extractRequestTimestamp(HttpServletRequest request) {
        return Optional
            .ofNullable(request.getAttribute(REQUEST_TIMESTAMP_KEY))
            .map(Timestamp.class::cast)
            .orElseThrow(RuntimeException::new);
    }

}
