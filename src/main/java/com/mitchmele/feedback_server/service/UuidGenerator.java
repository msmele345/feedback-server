package com.mitchmele.feedback_server.service;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UuidGenerator {

    public String generateStringUuid() {
        return UUID.randomUUID().toString();
    }
}
