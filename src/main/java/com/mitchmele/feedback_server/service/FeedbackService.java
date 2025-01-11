package com.mitchmele.feedback_server.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mitchmele.feedback_server.model.FeedbackRequest;
import com.mitchmele.feedback_server.model.ServiceResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final UuidGenerator uuidGenerator;
    private final RestTemplate feedbackRestTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();
    //publish to new topic for feedback?
    //call http function

    public ResponseEntity<ServiceResponse> uploadFeedBack(FeedbackRequest feedbackRequest) {
        final ServiceResponse response = new ServiceResponse();
        feedbackRequest.setFeedbackId(uuidGenerator.generateStringUuid());

        try {
            feedbackRestTemplate
                    .postForEntity("http://localhost:51353/api/postFeedback", feedbackRequest, Void.class);

            response.setStatus("Success");

            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            log.info("Error while uploading feedbackRequest: {}", e.getMessage());

            response.setHasError(true);
            response.setStatus("Upload failed. Error: " + e.getLocalizedMessage());

            return ResponseEntity.internalServerError().body(response);
        }
    }
}
