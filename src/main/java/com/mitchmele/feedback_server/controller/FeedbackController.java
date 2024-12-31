package com.mitchmele.feedback_server.controller;

import com.mitchmele.feedback_server.model.FeedbackRequest;
import com.mitchmele.feedback_server.model.ServiceResponse;
import com.mitchmele.feedback_server.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping("/feedback")
    public ResponseEntity<ServiceResponse> postFeedback(@RequestBody FeedbackRequest request) {
        final ServiceResponse response = new ServiceResponse();
        try {
            feedbackService.uploadFeedBack(request);
            response.setHasError(false);
            response.setStatus("Success");
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            log.info("Error while uploading feedback: {}", e.getMessage());
            response.setHasError(true);
            response.setStatus("Feedback Upload Error: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}
