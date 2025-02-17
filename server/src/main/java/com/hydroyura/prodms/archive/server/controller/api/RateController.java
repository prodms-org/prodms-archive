package com.hydroyura.prodms.archive.server.controller.api;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PATCH;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import com.hydroyura.prodms.archive.client.model.req.CreateRateReq;
import com.hydroyura.prodms.archive.client.model.req.PatchRateCountReq;
import com.hydroyura.prodms.archive.server.service.RateService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(
    value = "/api/v1/rates",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE)
public class RateController extends AbstractRestController {

    private final RateService rateService;


    @RequestMapping(method = GET, value = {"/{assembly}", "/{assembly}/"})
    public ResponseEntity<?> getAssembly(@PathVariable String assembly, HttpServletRequest request, @RequestParam boolean extended) {
        return null;
    }

    @RequestMapping(method = POST, value = {"", "/"})
    public ResponseEntity<?> create(@RequestBody CreateRateReq req, HttpServletRequest request) {
        return null;
    }

    @RequestMapping(method = DELETE, value = {"/{assembly}/{unit}", "/{assembly}/{unit}/"})
    public ResponseEntity<?> delete(@RequestBody CreateRateReq req, HttpServletRequest request) {
        return null;
    }

    @RequestMapping(method = PATCH, value = {"/{assembly}/{unit}", "/{assembly}/{unit}/"})
    public ResponseEntity<?> patchCount(@RequestBody PatchRateCountReq req, HttpServletRequest request) {
        return null;
    }

}
