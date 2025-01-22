package com.mitchmele.feedback_server.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserEventRequest {

    private String id;
    private String eventType;
    private String imageName;
    private String timestamp;
}
