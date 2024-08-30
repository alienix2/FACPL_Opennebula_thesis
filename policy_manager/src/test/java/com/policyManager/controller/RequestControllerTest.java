package com.policyManager.controller;

import com.policyManager.service.RequestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class RequestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RequestService requestService;

    @InjectMocks
    private RequestController requestController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(requestController).build();
    }

    @Test
    void getRequests_ShouldReturnListOfRequests() throws Exception {
        List<String> requests = Collections.singletonList("Request1");
        given(requestService.getRequests()).willReturn(ResponseEntity.ok(requests));

        mockMvc.perform(get("/requests")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[\"Request1\"]"));
    }

    @Test
    void getRequests_WhenFileNotFound_ShouldReturnNotFound() throws Exception {
        given(requestService.getRequests()).willReturn(ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonList("Requests file not found.")));

        mockMvc.perform(get("/requests")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().json("[\"Requests file not found.\"]"));
    }

    @Test
    void validateRequest_ShouldReturnValidationResult() throws Exception {
        String requestContent = "Mock Request Content";
        given(requestService.validateRequest(requestContent)).willReturn(ResponseEntity.ok("Validation successful"));

        mockMvc.perform(post("/requests/validate")
                .contentType(MediaType.APPLICATION_JSON)
                .content("Mock Request Content"))
                .andExpect(status().isOk())
                .andExpect(content().string("Validation successful"));
    }

    @Test
    void validateRequest_ShouldReturnBadRequest() throws Exception {
        String requestContent = "Mock Request Content";
        given(requestService.validateRequest(requestContent)).willReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Request validation failed"));

        mockMvc.perform(post("/requests/validate")
                .contentType(MediaType.APPLICATION_JSON)
                .content("Mock Request Content"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Request validation failed"));
    }

    @Test
    void submitRequest_ShouldReturnOk() throws Exception {
        given(requestService.submitRequest()).willReturn(ResponseEntity.ok("Request executed"));

        mockMvc.perform(post("/requests/submit")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Request executed"));
    }

    @Test
    void submitRequest_ShouldReturnBadRequest() throws Exception {
        given(requestService.submitRequest()).willReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Something went wrong"));

        mockMvc.perform(post("/requests/submit")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Something went wrong"));
    }
}
