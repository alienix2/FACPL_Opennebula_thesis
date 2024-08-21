package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Collections;
import java.util.List;

@SpringBootApplication
@RestController
public class Application {

    private static final String POLICIES_FILE_PATH = "facpl_data/policies.fpl";
    private static final String REQUESTS_FILE_PATH = "facpl_data/requests.fpl";

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @GetMapping("/policies")
    public ResponseEntity<List<String>> getPolicies() throws IOException {
        Path path = Paths.get(POLICIES_FILE_PATH);
        if (!Files.exists(path)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonList("Policies file not found."));
        }
        List<String> policies = Files.readAllLines(path);
        return ResponseEntity.ok(policies);
    }


    @PostMapping("/update-policies")
    public ResponseEntity<String> updatePolicies(@RequestBody List<String> policies) throws IOException {
        Files.write(Paths.get(POLICIES_FILE_PATH),
                    policies,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);
        return ResponseEntity.ok("Policies updated successfully");
    }

    @GetMapping("/requests")
    public ResponseEntity<List<String>> getRequests() throws IOException {
        Path path = Paths.get(REQUESTS_FILE_PATH);
        if (!Files.exists(path)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonList("Requests file not found."));
        }
        List<String> requests = Files.readAllLines(path);
        return ResponseEntity.ok(requests);
    }


    @PostMapping("/submit-request")
    public ResponseEntity<String> submitRequest(@RequestBody String request) throws IOException {
        Files.write(Paths.get(REQUESTS_FILE_PATH),
                    request.getBytes(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);
        return ResponseEntity.ok("Request submitted successfully");
    }
}
