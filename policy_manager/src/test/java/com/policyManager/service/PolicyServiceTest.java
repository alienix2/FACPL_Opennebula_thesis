package com.policyManager.service;

import com.policyManager.utils.FACPLFileValidator;
import entryPoint.ApplyPolicy;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PolicyServiceTest {

    @InjectMocks
    private PolicyService policyService;

    @Mock
    private ApplyPolicy applyPolicy;

    private static final String TEST_POLICIES_PATH = "test-policies.txt";
    private static final String TEST_TEMP_PATH = "test-temp.txt";
    private static final String TEST_DIR_PATH = "test-compile";

    @BeforeEach
    void setUp() throws IOException {
        ReflectionTestUtils.setField(policyService, "policiesFilePath", TEST_POLICIES_PATH);
        ReflectionTestUtils.setField(policyService, "tempFilePath", TEST_TEMP_PATH);
        ReflectionTestUtils.setField(policyService, "compilePath", TEST_DIR_PATH);
        ReflectionTestUtils.setField(policyService, "compileLog", "test-compile.log");
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(Paths.get(TEST_POLICIES_PATH));
        Files.deleteIfExists(Paths.get(TEST_TEMP_PATH));
        Files.deleteIfExists(Paths.get(TEST_DIR_PATH));
    }

    @Test
    void getPolicies_WhenFileExists_ShouldReturnPolicies() throws IOException {
        List<String> expectedPolicies = Arrays.asList("Policy1", "Policy2");
        Path path = Paths.get(TEST_POLICIES_PATH);
        Files.write(path, expectedPolicies);

        ResponseEntity<List<String>> response = policyService.getPolicies();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedPolicies, response.getBody());
    }

    @Test
    void getPolicies_WhenFileDoesNotExist_ShouldReturnNotFound() throws IOException {
        Path path = Paths.get(TEST_POLICIES_PATH);
        Files.deleteIfExists(path);

        ResponseEntity<List<String>> response = policyService.getPolicies();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(Collections.singletonList("Policies file not found."), response.getBody());
    }
    
    @Test
    void validatePolicies_WhenValid_ShouldReturnOk() throws IOException {
        List<String> policies = Arrays.asList("Policy1", "Policy2");
        
        try (MockedStatic<FACPLFileValidator> mockedStatic = mockStatic(FACPLFileValidator.class)) {
            mockedStatic.when(() -> FACPLFileValidator.validate(anyString())).thenReturn(true);

            ResponseEntity<String> response = policyService.validatePolicies(policies);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals("Valid FACPL file!", response.getBody());
        }
    }

    @Test
    void validatePolicies_WhenInvalid_ShouldReturnBadRequest() throws IOException {
        List<String> policies = Arrays.asList("Policy1", "Policy2");
        
        try (MockedStatic<FACPLFileValidator> mockedStatic = mockStatic(FACPLFileValidator.class)) {
            mockedStatic.when(() -> FACPLFileValidator.validate(anyString())).thenReturn(false);

            ResponseEntity<String> response = policyService.validatePolicies(policies);

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertEquals("Policy validation failed", response.getBody());
        }
    }

    @Test
    void submitPolicies_WhenSuccessful_ShouldReturnOk() throws Exception {
        doNothing().when(applyPolicy).execute(any(String[].class));

        ResponseEntity<String> response = policyService.submitPolicies();
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Policy applied", response.getBody());

        verify(applyPolicy, times(1)).execute(any(String[].class));
    }

    @Test
    void submitPolicies_WhenExceptionOccurs_ShouldReturnBadRequest() throws Exception {
        doThrow(new RuntimeException("Test Exception")).when(applyPolicy).execute(any(String[].class));

        ResponseEntity<String> response = policyService.submitPolicies();

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Something went wrong", response.getBody());
    }
}
