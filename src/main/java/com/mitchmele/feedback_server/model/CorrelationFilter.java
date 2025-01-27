package com.mitchmele.feedback_server.model;

public enum CorrelationFilter {
    FEEDBACK("1"),
    USER_EVENT("2");

    private final String correlationId;

    CorrelationFilter(String correlationId) {
        this.correlationId = correlationId;
    }

    @Override
    public String toString() {
        return correlationId;
    }
}
