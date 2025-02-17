package com.hydroyura.prodms.archive.server.controller.api;

import static com.hydroyura.prodms.archive.server.controller.api.TestControllerUtils.MAP_TYPE_FOR_PARAMS;
import static com.hydroyura.prodms.archive.server.controller.api.TestControllerUtils.UNIT_NAME_1;
import static com.hydroyura.prodms.archive.server.controller.api.TestControllerUtils.UNIT_NUMBER_1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hydroyura.prodms.archive.client.model.req.CreateUnitReq;
import com.hydroyura.prodms.archive.client.model.req.ListUnitsReq;
import com.hydroyura.prodms.archive.client.model.req.PatchUnitReq;
import com.hydroyura.prodms.archive.client.model.res.GetUnitRes;
import com.hydroyura.prodms.archive.client.model.res.ListUnitsRes;
import com.hydroyura.prodms.archive.server.exception.model.ValidationException;
import com.hydroyura.prodms.archive.server.service.UnitService;
import com.hydroyura.prodms.archive.server.validation.ValidationManager;
import com.hydroyura.prodms.archive.server.validation.enums.NumberKey;
import com.hydroyura.prodms.archive.server.validation.model.WrapNumber;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.SimpleErrors;

@WebMvcTest(controllers = UnitController.class)
class UnitControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ValidationManager validationManager;

    @MockBean
    private UnitService unitService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void get_OK() throws Exception {
        Mockito
            .when(unitService.get(UNIT_NUMBER_1))
            .thenReturn(Optional.of(new GetUnitRes()));

        mockMvc
            .perform(MockMvcRequestBuilders
                .get("/api/v1/units/" + UNIT_NUMBER_1)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void get_NOT_FOUND() throws Exception {
        Mockito
            .when(unitService.get(UNIT_NUMBER_1))
            .thenReturn(Optional.empty());

        mockMvc
            .perform(MockMvcRequestBuilders
                .get("/api/v1/units/" + UNIT_NUMBER_1)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void get_BAD_REQUEST() throws Exception {
        var wrapNumber = new WrapNumber<>(UNIT_NUMBER_1, String.class, NumberKey.UNIT);
        var errors = new SimpleErrors(wrapNumber);
        Mockito
            .doThrow(new ValidationException(errors, "TEST MESSAGE"))
            .when(validationManager)
            .validate(wrapNumber, WrapNumber.class);

        mockMvc
            .perform(MockMvcRequestBuilders
                .get("/api/v1/units/" + UNIT_NUMBER_1)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void list_OK() throws Exception {
        var req = new ListUnitsReq();
        req.setNameLike("NAME_LIKE");
        req.setNumberLike("NUMBER_LIKE");
        req.setPageNum(10);
        req.setItemsPerPage(50);
        req.setTypeIn(List.of("TYPE_1", "TYPE_2"));
        req.setStatusIn(List.of("STATUS_1", "STATUS_2"));

        Mockito
            .when(unitService.list(req))
            .thenReturn(new ListUnitsRes());

        mockMvc
            .perform(MockMvcRequestBuilders
                .get("/api/v1/units")
                .contentType(MediaType.APPLICATION_JSON)
                .params(getMultiValueMapFor(req)))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void list_BAD_REQUEST() throws Exception {
        var req = new ListUnitsReq();
        req.setNameLike("NAME_LIKE");
        req.setNumberLike("NUMBER_LIKE");
        req.setPageNum(10);
        req.setItemsPerPage(50);
        req.setTypeIn(List.of("TYPE_1", "TYPE_2"));
        req.setStatusIn(List.of("STATUS_1", "STATUS_2"));

        var errors = new SimpleErrors(req);

        Mockito
            .doThrow(new ValidationException(errors, "TEST MESSAGE"))
            .when(validationManager)
            .validate(req, ListUnitsReq.class);

        mockMvc
            .perform(MockMvcRequestBuilders
                .get("/api/v1/units")
                .contentType(MediaType.APPLICATION_JSON)
                .params(getMultiValueMapFor(req)))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void create_OK() throws Exception {
        var req = new CreateUnitReq();
        req.setName(UNIT_NAME_1);
        req.setNumber(UNIT_NUMBER_1);

        Mockito
            .doNothing()
            .when(unitService)
            .create(req);

        mockMvc
            .perform(MockMvcRequestBuilders
                .post("/api/v1/units")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void create_BAD_REQUEST() throws Exception {
        var req = new CreateUnitReq();
        req.setName(UNIT_NAME_1);
        req.setNumber(UNIT_NUMBER_1);

        var errors = new SimpleErrors(req);

        Mockito
            .doThrow(new ValidationException(errors, "TEST MESSAGE"))
            .when(validationManager)
            .validate(req, CreateUnitReq.class);

        mockMvc
            .perform(MockMvcRequestBuilders
                .post("/api/v1/units")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void delete_OK() throws Exception {
        Mockito
            .when(unitService.delete(UNIT_NUMBER_1))
            .thenReturn(Optional.of(UNIT_NUMBER_1));

        mockMvc
            .perform(MockMvcRequestBuilders
                .delete("/api/v1/units/" + UNIT_NUMBER_1)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void delete_NOT_FOUND() throws Exception {
        Mockito
            .when(unitService.delete(UNIT_NUMBER_1))
            .thenReturn(Optional.empty());

        mockMvc
            .perform(MockMvcRequestBuilders
                .delete("/api/v1/units/" + UNIT_NUMBER_1)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void delete_BAD_REQUEST() throws Exception {
        var wrapNumber = new WrapNumber<>(UNIT_NUMBER_1, String.class, NumberKey.UNIT);
        var errors = new SimpleErrors(wrapNumber);
        Mockito
            .doThrow(new ValidationException(errors, "TEST MESSAGE"))
            .when(validationManager)
            .validate(wrapNumber, WrapNumber.class);

        mockMvc
            .perform(MockMvcRequestBuilders
                .delete("/api/v1/units/" + UNIT_NUMBER_1)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void patch_OK() throws Exception {
        var req = new PatchUnitReq();
        req.setName("TEST NAME");
        req.setStatus(1);
        Mockito
            .when(unitService.patch(UNIT_NUMBER_1, req))
            .thenReturn(Optional.of(UNIT_NUMBER_1));

        mockMvc
            .perform(MockMvcRequestBuilders
                .patch("/api/v1/units/" + UNIT_NUMBER_1)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(req)))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void patch_NOT_FOUND() throws Exception {
        var req = new PatchUnitReq();
        req.setName("TEST NAME");
        req.setStatus(1);
        Mockito
            .when(unitService.patch(UNIT_NUMBER_1, req))
            .thenReturn(Optional.empty());

        mockMvc
            .perform(MockMvcRequestBuilders
                .patch("/api/v1/units/" + UNIT_NUMBER_1)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(req)))
            .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void patch_BAD_REQUEST() throws Exception {
        var req = new PatchUnitReq();
        req.setName("TEST NAME");
        req.setStatus(1);
        var errors = new SimpleErrors(req);
        Mockito
            .doThrow(new ValidationException(errors, "TEST MESSAGE"))
            .when(validationManager)
            .validate(req, PatchUnitReq.class);

        mockMvc
            .perform(MockMvcRequestBuilders
                .patch("/api/v1/units/" + UNIT_NUMBER_1)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(req)))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @SuppressWarnings("unchecked")
    private MultiValueMap<String, String> getMultiValueMapFor(ListUnitsReq req) throws Exception {
        var jsonString = objectMapper.writeValueAsString(req);
        Map<String, Object> map = objectMapper.readValue(jsonString, MAP_TYPE_FOR_PARAMS);
        Map<String, List<String>> convertedMap = map.entrySet()
            .stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                e -> {
                    Object value = e.getValue();
                    List<String> list = new ArrayList<>();
                    if (value != null) {
                        if (value instanceof Collection<?>) {
                            list.addAll((List<String>) value);
                        } else {
                            list.add(value.toString());
                        }
                    }
                    return list;
                }
            ));
        return new LinkedMultiValueMap<>(convertedMap);
    }

}