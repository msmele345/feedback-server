package com.mitchmele.feedback_server.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackItem {

    private String feedbackId;
    private String content;
    private String submissionTime;

}
