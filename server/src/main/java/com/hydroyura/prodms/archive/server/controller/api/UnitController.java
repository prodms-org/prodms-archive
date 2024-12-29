package com.hydroyura.prodms.archive.server.controller.api;

import static com.hydroyura.prodms.archive.server.utils.SharedStringConstants.REQUEST_ATTR_UUID_KEY;
import static com.hydroyura.prodms.archive.server.utils.SharedStringConstants.REQUEST_TIMESTAMP_KEY;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PATCH;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import com.hydroyura.prodms.archive.client.model.api.ApiSuccessResponse;
import com.hydroyura.prodms.archive.client.model.req.CreateUnitReq;
import com.hydroyura.prodms.archive.client.model.req.ListUnitsReq;
import com.hydroyura.prodms.archive.client.model.req.PatchUnitReq;
import com.hydroyura.prodms.archive.client.model.res.GetUnitRes;
import com.hydroyura.prodms.archive.client.model.res.ListUnitsRes;
import com.hydroyura.prodms.archive.server.service.UnitService;
import com.hydroyura.prodms.archive.server.validation.ValidationManager;
import com.hydroyura.prodms.archive.server.validation.model.WrapNumber;
import jakarta.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
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
public class UnitController extends AbstractRestController {

    private final ValidationManager validationManager;
    private final UnitService unitService;


    @RequestMapping(method = GET, value = {"", "/"})
    public ResponseEntity<ApiSuccessResponse<ListUnitsRes>> list(ListUnitsReq req, HttpServletRequest request) {
        validationManager.validate(req, ListUnitsReq.class);
        var res = unitService.list(req);
        return ResponseEntity.ok(buildApiResponse(res, request));
    }

    @RequestMapping(method = POST, value = {"", "/"})
    public ResponseEntity<ApiSuccessResponse<Void>> create(@RequestBody CreateUnitReq req, HttpServletRequest request) {
        validationManager.validate(req, CreateUnitReq.class);
        unitService.create(req);
        return ResponseEntity.ok(buildApiResponse(null, request));
    }

    @RequestMapping(method = DELETE, value = {"/{number}", "/{number}/"})
    public ResponseEntity<ApiSuccessResponse<Void>> delete(@PathVariable String number, HttpServletRequest request) {
        validationManager.validate(new WrapNumber(number), WrapNumber.class);
        unitService.delete(number);
        return ResponseEntity.ok(buildApiResponse(null, request));
    }

    @RequestMapping(method = GET, value = {"/{number}", "/{number}/"})
    public ResponseEntity<ApiSuccessResponse<GetUnitRes>> get(@PathVariable String number, HttpServletRequest request) {
        validationManager.validate(new WrapNumber(number), WrapNumber.class);
        var res = unitService.get(number);
        return ResponseEntity.ok(buildApiResponse(res, request));
    }

    @RequestMapping(method = PATCH, value = {"/{number}", "/{number}/"})
    public ResponseEntity<ApiSuccessResponse<Void>> patch(@PathVariable String number,
                                                          @RequestBody PatchUnitReq req,
                                                          HttpServletRequest request) {
        validationManager.validate(req, PatchUnitReq.class);
        unitService.patch(number, req);
        return ResponseEntity.ok(buildApiResponse(null, request));
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

    private static <T> ApiSuccessResponse<T> buildApiResponse(T res, HttpServletRequest request) {
        ApiSuccessResponse<T> apiRes = new ApiSuccessResponse<>();
        apiRes.setData(res);
        apiRes.setId(extractRequestUUID(request));
        apiRes.setTimestamp(extractRequestTimestamp(request));
        return apiRes;
    }

    private void populateRequestWithDefaults(ListUnitsReq req) {
        if (Objects.isNull(req.getPageNum())) {

        }

        if (Objects.isNull(req.getItemsPerPage())) {

        }

        if (Objects.isNull(req.getSortCode())) {

        }
    }

}
