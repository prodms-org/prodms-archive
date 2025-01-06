package com.hydroyura.prodms.archive.server.controller.api;

import static com.hydroyura.prodms.archive.server.SharedConstants.REQUEST_ATTR_UUID_KEY;
import static com.hydroyura.prodms.archive.server.SharedConstants.REQUEST_TIMESTAMP_KEY;
import static com.hydroyura.prodms.archive.server.SharedConstants.RESPONSE_ERROR_MSG_ENTITY_NOT_FOUND;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import com.hydroyura.prodms.archive.client.model.api.ApiRes;
import com.hydroyura.prodms.archive.client.model.req.CreateUnitReq;
import com.hydroyura.prodms.archive.client.model.req.ListUnitsReq;
import com.hydroyura.prodms.archive.client.model.res.GetUnitRes;
import com.hydroyura.prodms.archive.client.model.res.ListUnitsRes;
import com.hydroyura.prodms.archive.server.controller.swagger.UnitDocumentedController;
import com.hydroyura.prodms.archive.server.service.UnitService;
import com.hydroyura.prodms.archive.server.validation.ValidationManager;
import com.hydroyura.prodms.archive.server.validation.enums.NumberKey;
import com.hydroyura.prodms.archive.server.validation.model.WrapNumber;
import jakarta.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(
    value = "/api/v1/units",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE)
public class UnitController implements UnitDocumentedController {

    private final ValidationManager validationManager;
    private final UnitService unitService;


    @Override
    @RequestMapping(method = GET, value = "/{number}")
    public ResponseEntity<ApiRes<GetUnitRes>> get(@PathVariable String number, HttpServletRequest request) {
        validationManager.validate(new WrapNumber(number, String.class, NumberKey.UNIT), WrapNumber.class);
        var res = unitService.get(number);
        return buildApiResponseOkOrNotFound(res, request, number);
    }

    @Override
    @RequestMapping(method = GET, value = "")
    public ResponseEntity<ApiRes<ListUnitsRes>> list(ListUnitsReq req, HttpServletRequest request) {
        validationManager.validate(req, ListUnitsReq.class);
        var res = unitService.list(req);
        return buildApiResponseOk(res, request);
    }

    @Override
    @RequestMapping(method = POST, value = "")
    public ResponseEntity<ApiRes<Void>> create(@RequestBody CreateUnitReq req, HttpServletRequest request) {
        validationManager.validate(req, CreateUnitReq.class);
        unitService.create(req);
        return buildApiResponseNotContent(request);
    }



    private static <T> ResponseEntity<ApiRes<T>> buildApiResponseOkOrNotFound(Optional<T> data, HttpServletRequest req, Object number) {
        ApiRes<T> apiResponse = new ApiRes<>();
        apiResponse.setId(extractRequestUUID(req));
        apiResponse.setTimestamp(extractRequestTimestamp(req));
        data.ifPresentOrElse(
            apiResponse::setData,
            () -> apiResponse.getErrors().add(RESPONSE_ERROR_MSG_ENTITY_NOT_FOUND.formatted(number))
        );

        ResponseEntity<ApiRes<T>> responseEntity;
        if (data.isPresent()) {
            responseEntity = new ResponseEntity<>(apiResponse, HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
        }

        return responseEntity;
    }

    private static <T> ResponseEntity<ApiRes<T>> buildApiResponseOk(T data, HttpServletRequest req) {
        ApiRes<T> apiResponse = new ApiRes<>();
        apiResponse.setId(extractRequestUUID(req));
        apiResponse.setTimestamp(extractRequestTimestamp(req));
        apiResponse.setData(data);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    private static <T> ResponseEntity<ApiRes<T>> buildApiResponseNotContent(HttpServletRequest req) {
        ApiRes<T> apiResponse = new ApiRes<>();
        apiResponse.setId(extractRequestUUID(req));
        apiResponse.setTimestamp(extractRequestTimestamp(req));
        return new ResponseEntity<>(apiResponse, HttpStatus.NO_CONTENT);
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




















    /*

    @RequestMapping(method = DELETE, value = {"/{number}", "/{number}/"})
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String number, HttpServletRequest request) {
        validationManager.validate(new WrapNumber(number), WrapNumber.class);
        unitService.delete(number);
        return ResponseEntity.ok(buildApiResponse(null, request));
    }

    @RequestMapping(method = PATCH, value = {"/{number}", "/{number}/"})
    public ResponseEntity<ApiResponse<Void>> patch(@PathVariable String number,
                                                   @RequestBody PatchUnitReq req,
                                                   HttpServletRequest request) {
        validationManager.validate(req, PatchUnitReq.class);
        unitService.patch(number, req);
        return ResponseEntity.ok(buildApiResponse(null, request));
    }




    private static <T> ApiResponse<T> buildApiResponse(T res, HttpServletRequest request) {
        ApiResponse<T> apiRes = new ApiResponse<>();
        apiRes.setData(res);
        apiRes.setId(extractRequestUUID(request));
        apiRes.setTimestamp(extractRequestTimestamp(request));
        return apiRes;
    }
    */
}
