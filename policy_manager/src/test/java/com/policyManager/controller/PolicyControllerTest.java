package com.policyManager.controller;

import com.policyManager.service.PolicyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PolicyController.class)
class PolicyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PolicyService policyService;

    @Test
    void getPolicies_ShouldReturnPolicyFileContent() throws Exception {
        String policyContent = "Mock Policy Content";
        given(policyService.getPolicies()).willReturn(ResponseEntity.ok(Arrays.asList(policyContent)));

        mockMvc.perform(get("/policies"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[\"Mock Policy Content\"]"));
    }

    @Test
    void getPolicies_WhenFileNotFound_ShouldReturnNotFound() throws Exception {
        given(policyService.getPolicies()).willReturn(ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonList("Policies file not found.")));

        mockMvc.perform(get("/policies")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[\"Policies file not found.\"]"));
    }
    
    @Test
    void validatePolicies_ShouldReturnOk() throws Exception {
        String policyContent = "Mock Policy Content";
        given(policyService.validatePolicies(Arrays.asList(policyContent))).willReturn(ResponseEntity.ok("Validation successful"));

        mockMvc.perform(post("/policies/validate")
                .contentType(MediaType.APPLICATION_JSON)
                .content("[\"Mock Policy Content\"]"))
                .andExpect(status().isOk())
                .andExpect(content().string("Validation successful"));
    }
    
    @Test
    void validatePolicies_ShouldReturnBadRequest() throws Exception {
        String policyContent = "Mock Policy Content";
        given(policyService.validatePolicies(Arrays.asList(policyContent))).willReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Policy validation failed"));

        mockMvc.perform(post("/policies/validate")
                .contentType(MediaType.APPLICATION_JSON)
                .content("[\"Mock Policy Content\"]"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Policy validation failed"));
    }

    @Test
    void submitPolicies_ShouldReturnOk() throws Exception {
        given(policyService.submitPolicies()).willReturn(ResponseEntity.ok("Submission successful"));

        mockMvc.perform(post("/policies/submit"))
                .andExpect(status().isOk())
                .andExpect(content().string("Submission successful"));
    }
    
    @Test
    void submitPolicies_ShouldReturnBadRequest() throws Exception {
        given(policyService.submitPolicies()).willReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Submission failed"));

        mockMvc.perform(post("/policies/submit"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Submission failed"));
    }
}
