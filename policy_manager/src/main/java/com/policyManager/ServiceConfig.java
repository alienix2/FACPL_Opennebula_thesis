package com.policyManager;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import entryPoint.RequestExecution;
import entryPoint.ApplyPolicy;

@Configuration
public class ServiceConfig {

    @Value("${logging.compile.file.name}")
    private String compileLog;

    @Value("${facpl.compile.folderpath}")
    private String compilePath;

    @Bean
    public ApplyPolicy applyPolicy() throws IOException {
        return new ApplyPolicy(compileLog, compilePath);
    }

    @Bean
    public RequestExecution requestExecution() throws IOException {
        return new RequestExecution(compileLog, compilePath);
    }
}