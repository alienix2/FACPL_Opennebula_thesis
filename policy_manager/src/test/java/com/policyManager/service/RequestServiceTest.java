package com.policyManager.service;

import com.policyManager.utils.FACPLFileValidator;
import entryPoint.RequestExecution;
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
class RequestServiceTest {

    @InjectMocks
    private RequestService requestService;

    @Mock
    private RequestExecution requestExecution;

    private static final String TEST_REQUESTS_PATH = "test-requests.txt";
    private static final String TEST_TEMP_PATH = "test-temp.txt";
    private static final String TEST_DIR_PATH = "test-compile";

    @BeforeEach
    void setUp() throws IOException {
        ReflectionTestUtils.setField(requestService, "requestsFilePath", TEST_REQUESTS_PATH);
        ReflectionTestUtils.setField(requestService, "tempFilePath", TEST_TEMP_PATH);
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(Paths.get(TEST_REQUESTS_PATH));
        Files.deleteIfExists(Paths.get(TEST_TEMP_PATH));
        Files.deleteIfExists(Paths.get(TEST_DIR_PATH));
    }

    @Test
    void getRequests_WhenFileExists_ShouldReturnRequests() throws IOException {
        List<String> expectedRequests = Arrays.asList("Request1", "Request2");
        Path path = Paths.get(TEST_REQUESTS_PATH);
        Files.write(path, expectedRequests);

        ResponseEntity<List<String>> response = requestService.getRequests();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedRequests, response.getBody());
    }

    @Test
    void getRequests_WhenFileDoesNotExist_ShouldReturnNotFound() throws IOException {
        Path path = Paths.get(TEST_REQUESTS_PATH);
        Files.deleteIfExists(path);

        ResponseEntity<List<String>> response = requestService.getRequests();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(Collections.singletonList("Requests file not found."), response.getBody());
    }

    @Test
    void validateRequest_WhenValid_ShouldReturnOk() throws IOException {
        String request = "Valid Request";

        try (MockedStatic<FACPLFileValidator> mockedStatic = mockStatic(FACPLFileValidator.class)) {
            mockedStatic.when(() -> FACPLFileValidator.validate(anyString())).thenReturn(true);

            ResponseEntity<String> response = requestService.validateRequest(request);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals("Valid FACPL file!", response.getBody());
        }
    }

    @Test
    void validateRequest_WhenInvalid_ShouldReturnBadRequest() throws IOException {
        String request = "Invalid Request";

        try (MockedStatic<FACPLFileValidator> mockedStatic = mockStatic(FACPLFileValidator.class)) {
            mockedStatic.when(() -> FACPLFileValidator.validate(anyString())).thenReturn(false);

            ResponseEntity<String> response = requestService.validateRequest(request);

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertEquals("Request validation failed", response.getBody());
        }
    }

    @Test
    void submitRequest_WhenSuccessful_ShouldReturnOk() throws Exception {
        doNothing().when(requestExecution).execute(any(String[].class));

        ResponseEntity<String> response = requestService.submitRequest();
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Request executed", response.getBody());
    }

    @Test
    void submitRequest_WhenExceptionOccurs_ShouldReturnBadRequest() throws Exception {
        doThrow(new RuntimeException("Test Exception")).when(requestExecution).execute(any(String[].class));

        ResponseEntity<String> response = requestService.submitRequest();

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Something went wrong", response.getBody());
    }
}