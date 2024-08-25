package com.policyManager.controller;

import com.policyManager.service.PolicyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/policies")
public class PolicyController {

    private final PolicyService policyService;

    public PolicyController(PolicyService policyService) {
        this.policyService = policyService;
    }

    @GetMapping
    public ResponseEntity<List<String>> getPolicies() throws IOException {
        return policyService.getPolicies();
    }

    @PostMapping("/validate")
    public ResponseEntity<String> validatePolicies(@RequestBody List<String> policies) throws IOException {
        return policyService.validatePolicies(policies);
    }

    @PostMapping("/submit")
    public ResponseEntity<String> submitPolicies() throws IOException {
        return policyService.submitPolicies();
    }
}
