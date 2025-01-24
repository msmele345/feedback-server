package com.mitchmele.feedback_server.controller;

import com.mitchmele.feedback_server.model.FeedbackRequest;
import com.mitchmele.feedback_server.model.ServiceResponse;
import com.mitchmele.feedback_server.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@CrossOrigin //dev/local only
@RestController
@RequestMapping("/api/v2")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping("/feedback")
    public ResponseEntity<ServiceResponse> postFeedback(@RequestBody FeedbackRequest request) {
        return feedbackService.saveFeedback(request);
    }
}
