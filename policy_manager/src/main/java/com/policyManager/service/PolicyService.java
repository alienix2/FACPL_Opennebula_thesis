package com.policyManager.service;

import entryPoint.ApplyPolicy;

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
public class PolicyService {

    @Value("${facpl.policies.filepath}") String policiesFilePath;

    @Value("${facpl.temp.filepath}") String tempFilePath;
    
    private final ApplyPolicy applyPolicy;

    public PolicyService(ApplyPolicy applyPolicy) {
        this.applyPolicy = applyPolicy;
    }
    
    public ResponseEntity<List<String>> getPolicies() throws IOException {
        Path path = Paths.get(policiesFilePath);
        if (!Files.exists(path)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonList("Policies file not found."));
        }
        List<String> policies = Files.readAllLines(path);
        return ResponseEntity.ok(policies);
    }

    public ResponseEntity<String> validatePolicies(List<String> policies) throws IOException {
        Files.write(Paths.get(tempFilePath), policies, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        if (FACPLFileValidator.validate(tempFilePath)) {
            Files.move(Paths.get(tempFilePath), Paths.get(policiesFilePath), StandardCopyOption.REPLACE_EXISTING);
            return ResponseEntity.ok("Valid FACPL file!");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Policy validation failed");
        }
    }

    public ResponseEntity<String> submitPolicies() throws IOException {
        try {
            applyPolicy.execute(new String[]{policiesFilePath});
            return ResponseEntity.ok("Policy applied");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Something went wrong");
        }
    }
}

