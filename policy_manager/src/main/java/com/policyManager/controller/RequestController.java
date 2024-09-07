package com.policyManager.controller;

import com.policyManager.service.RequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/requests")
public class RequestController {

    private final RequestService requestService;

    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @GetMapping
    public ResponseEntity<List<String>> getRequests() throws IOException {
        return requestService.getRequests();
    }

    @PostMapping("/validate")
    public ResponseEntity<String> validateRequest(@RequestBody String request) throws IOException {
        return requestService.validateRequest(request);
    }

    @PostMapping("/submit")
    public ResponseEntity<String> submitRequest() throws Exception {
        return requestService.submitRequest();
    }
}
