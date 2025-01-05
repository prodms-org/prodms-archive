package com.hydroyura.prodms.archive.server.controller.api;

import static com.hydroyura.prodms.archive.server.controller.api.TestControllerUtils.UNIT_NUMBER_1;

import com.hydroyura.prodms.archive.client.model.res.GetUnitRes;
import com.hydroyura.prodms.archive.server.exception.model.ValidationException;
import com.hydroyura.prodms.archive.server.service.UnitService;
import com.hydroyura.prodms.archive.server.validation.ValidationManager;
import com.hydroyura.prodms.archive.server.validation.enums.NumberKey;
import com.hydroyura.prodms.archive.server.validation.model.WrapNumber;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.validation.SimpleErrors;

@WebMvcTest(controllers = UnitController.class)
class UnitControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ValidationManager validationManager;

    @MockBean
    private UnitService unitService;


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
        var wrapNumber = new WrapNumber(UNIT_NUMBER_1, String.class, NumberKey.UNIT);
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

}