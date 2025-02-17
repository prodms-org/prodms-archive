package com.hydroyura.prodms.archive.server.controller.swagger;

import com.hydroyura.prodms.archive.client.model.api.ApiRes;
import com.hydroyura.prodms.archive.client.model.req.CreateUnitReq;
import com.hydroyura.prodms.archive.client.model.req.ListUnitsReq;
import com.hydroyura.prodms.archive.client.model.req.PatchUnitReq;
import com.hydroyura.prodms.archive.client.model.res.GetUnitRes;
import com.hydroyura.prodms.archive.client.model.res.ListUnitsRes;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface UnitDocumentedController {

    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            content = {@Content(schema = @Schema(implementation = ApiResGetUnitResSuccess.class))},
            description = "Success"
        ),
        @ApiResponse(
            responseCode = "400",
            content = {@Content(schema = @Schema(implementation = ApiResGetUnitResBadRequest.class))},
            description = "Unit number doesn't correspond required rules"
        ),
        @ApiResponse(
            responseCode = "404",
            content = {@Content(schema = @Schema(implementation = ApiResGetUnitResNotFount.class))},
            description = "Unit with getting number doesn't exist"
        )
    })
    ResponseEntity<ApiRes<GetUnitRes>> get(@PathVariable String number, HttpServletRequest request);

    class ApiResGetUnitResSuccess extends ApiRes<GetUnitRes> {
    }

    class ApiResGetUnitResNotFount extends ApiRes<Void> {
    }

    class ApiResGetUnitResBadRequest extends ApiRes<Void> {
    }


    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            content = {@Content(schema = @Schema(implementation = ApiResListUnitsResSuccess.class))},
            description = "Success"
        ),
        @ApiResponse(
            responseCode = "400",
            content = {@Content(schema = @Schema(implementation = ApiResListUnitsResBadRequest.class))},
            description = "Filter values isn't valid"
        )
    })
    ResponseEntity<ApiRes<ListUnitsRes>> list(ListUnitsReq req, HttpServletRequest request);

    class ApiResListUnitsResSuccess extends ApiRes<ListUnitsRes> {
    }

    class ApiResListUnitsResBadRequest extends ApiRes<Void> {
    }


    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            content = {@Content(schema = @Schema(implementation = ApiResCreateUnitSuccess.class))},
            description = "Unit was created successful"
        ),
        @ApiResponse(
            responseCode = "400",
            content = {@Content(schema = @Schema(implementation = ApiResCreateUnitBadRequest.class))},
            description = "Can't create unit with given data"
        )
    })
    ResponseEntity<ApiRes<Void>> create(@RequestBody CreateUnitReq req, HttpServletRequest request);

    class ApiResCreateUnitSuccess extends ApiRes<Void> {
    }

    class ApiResCreateUnitBadRequest extends ApiRes<Void> {
    }


    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            content = {@Content(schema = @Schema(implementation = ApiResDeleteUnitSuccess.class))},
            description = "Unit was deleted successful"
        ),
        @ApiResponse(
            responseCode = "400",
            content = {@Content(schema = @Schema(implementation = ApiResDeleteUnitBadRequest.class))},
            description = "Unit number doesn't correspond required rules"
        ),
        @ApiResponse(
            responseCode = "404",
            content = {@Content(schema = @Schema(implementation = ApiResDeleteUnitNotFound.class))},
            description = "Unit with getting number doesn't exist"
        )
    })
    ResponseEntity<ApiRes<Void>> delete(@PathVariable String number, HttpServletRequest request);

    class ApiResDeleteUnitSuccess extends ApiRes<Void> {
    }

    class ApiResDeleteUnitBadRequest extends ApiRes<Void> {
    }

    class ApiResDeleteUnitNotFound extends ApiRes<Void> {
    }

    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            content = {@Content(schema = @Schema(implementation = ApiResPatchUnitSuccess.class))},
            description = "Unit was patched successful"
        ),
        @ApiResponse(
            responseCode = "400",
            content = {@Content(schema = @Schema(implementation = ApiResPatchUnitBadRequest.class))},
            description = "Unit number doesn't correspond required rules"
        ),
        @ApiResponse(
            responseCode = "404",
            content = {@Content(schema = @Schema(implementation = ApiResPatchUnitNotFound.class))},
            description = "Unit with getting number doesn't exist"
        )
    })
    ResponseEntity<ApiRes<Void>> patch(@PathVariable String number,
                                       @RequestBody PatchUnitReq req,
                                       HttpServletRequest request);
    class ApiResPatchUnitSuccess extends ApiRes<Void> {
    }

    class ApiResPatchUnitBadRequest extends ApiRes<Void> {
    }

    class ApiResPatchUnitNotFound extends ApiRes<Void> {
    }


}