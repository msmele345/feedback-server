package com.mitchmele.feedback_server.service;

import com.azure.messaging.servicebus.ServiceBusMessage;
import com.azure.messaging.servicebus.ServiceBusSenderClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mitchmele.feedback_server.model.CorrelationFilter;
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
    //add managed identity for container app
    //allow the identity on the webapp

    public ResponseEntity<ServiceResponse> saveFeedback(FeedbackRequest feedbackRequest) {
        feedbackRequest.setFeedbackId(uuidGenerator.generateStringUuid());

        try {
            final String queueMsg = objectMapper.writeValueAsString(feedbackRequest);
            //move to new its own component. Feedback publisher?
            final ServiceBusMessage message = createServiceBusMessage(queueMsg);

            screensServiceBusSenderClient.sendMessage(message);
//            feedbackRestTemplate
//                    .postForEntity("http://localhost:51353/api/postFeedback", feedbackRequest, Void.class);

            log.info("Feedback Message Sent to Topic Successfully: {}", queueMsg);

            return new ResponseEntity<>(createResponse(true), HttpStatus.CREATED);
        } catch (Exception e) {
            log.info("Error while uploading feedbackRequest: {}", e.getMessage());

            return ResponseEntity.internalServerError().body(createResponse(false));
        }
    }

    private static ServiceBusMessage createServiceBusMessage(String queueMsg) {
        final ServiceBusMessage message = new ServiceBusMessage(queueMsg);
        message.setCorrelationId(CorrelationFilter.FEEDBACK.toString());
        return message;
    }

    private static ServiceResponse createResponse(boolean isSuccess) {
       ServiceResponse res = new ServiceResponse();
        res.setSuccess(isSuccess);
        res.setMessage(isSuccess ? "Processed Feedback Successfully" : "Feedback Processing Failed");
        return res;
    }
}
