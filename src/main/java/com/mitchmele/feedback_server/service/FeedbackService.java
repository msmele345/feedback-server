package com.mitchmele.feedback_server.service;

import com.azure.messaging.servicebus.ServiceBusMessage;
import com.azure.messaging.servicebus.ServiceBusSenderClient;
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

    private final ObjectMapper objectMapper;
    private final UuidGenerator uuidGenerator;
    private final RestTemplate feedbackRestTemplate;
    private final ServiceBusSenderClient screensServiceBusSenderClient;

    //publish function app
    //deploy screens to container services
    //call http function for onSuccess processing via azure function

    public ResponseEntity<ServiceResponse> saveFeedback(FeedbackRequest feedbackRequest) {
        final ServiceResponse response = new ServiceResponse();
        feedbackRequest.setFeedbackId(uuidGenerator.generateStringUuid());

        try {
            String queueMsg = objectMapper.writeValueAsString(feedbackRequest);
            //move to new its own component.
            screensServiceBusSenderClient.sendMessage(new ServiceBusMessage(queueMsg));

//            feedbackRestTemplate
//                    .postForEntity("http://localhost:51353/api/postFeedback", feedbackRequest, Void.class);

            response.setMessage("Processed Feedback Successfully");
            response.setSuccess(true);

            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            log.info("Error while uploading feedbackRequest: {}", e.getMessage());
            //fire message to service topic
            response.setSuccess(false);
            response.setMessage("Feedback Processing Failed. Error: " + e.getLocalizedMessage());

            return ResponseEntity.internalServerError().body(response);
        }
    }
}
