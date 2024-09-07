package com.policyManager.utils;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GlobalExceptionHandlerTest {
	@Test
	void testHandleGeneralException() {
		GlobalExceptionHandler handler = new GlobalExceptionHandler();
		Exception exception = new Exception("Test exception");
		ResponseEntity<String> response = handler.handleGeneralException(exception);
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
		assertEquals("An unexpected error occurred: Test exception", response.getBody());
	}

	@Test
	void testHandleIOException() {
		GlobalExceptionHandler handler = new GlobalExceptionHandler();
		IOException exception = new IOException("Test IO exception");
		ResponseEntity<String> response = handler.handleIOException(exception);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("File processing error: Test IO exception", response.getBody());
	}
}