package com.mitchmele.feedback_server.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackRequest {

    private String feedbackId;
    private String content;
    private String email;
    private String submissionTime;
}
