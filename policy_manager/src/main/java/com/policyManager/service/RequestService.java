package com.policyManager.service;

import entryPoint.RequestExecution;
import com.policyManager.utils.FACPLFileValidator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.*;
import java.util.Collections;
import java.util.List;

@Service
public class RequestService {

    @Value("${facpl.requests.filepath}")
    private String requestsFilePath;

    @Value("${facpl.temp.filepath}")
    private String tempFilePath;

    private final RequestExecution requestExecution;

    public RequestService(RequestExecution requestExecution) {
        this.requestExecution = requestExecution;
    }

    public ResponseEntity<List<String>> getRequests() throws IOException {
        Path path = Paths.get(requestsFilePath);
        if (!Files.exists(path)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonList("Requests file not found."));
        }
        List<String> requests = Files.readAllLines(path);
        return ResponseEntity.ok(requests);
    }

    public ResponseEntity<String> validateRequest(String request) throws IOException {
        Files.write(Paths.get(tempFilePath), request.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        if (FACPLFileValidator.validate(tempFilePath)) {
            Files.move(Paths.get(tempFilePath), Paths.get(requestsFilePath), StandardCopyOption.REPLACE_EXISTING);
            return ResponseEntity.ok("Valid FACPL file!");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Request validation failed");
        }
    }

    public ResponseEntity<String> submitRequest() throws IOException {
        try {
            requestExecution.execute(new String[]{requestsFilePath});
            return ResponseEntity.ok("Request executed");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Something went wrong");
        }
    }
}
