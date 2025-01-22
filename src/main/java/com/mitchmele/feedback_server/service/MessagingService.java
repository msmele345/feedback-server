package com.mitchmele.feedback_server.service;

import com.azure.messaging.servicebus.ServiceBusMessage;
import com.azure.messaging.servicebus.ServiceBusSenderClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mitchmele.feedback_server.model.ServiceResponse;
import com.mitchmele.feedback_server.model.UserEventRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class MessagingService {

    private final ObjectMapper objectMapper;
    private final ServiceBusSenderClient screensServiceBusSenderClient;

    public ServiceResponse sendMessageToTopic(UserEventRequest userEventRequest) {
        String msgContent = "";
        try {
            msgContent = objectMapper.writeValueAsString(userEventRequest);

            ServiceBusMessage msg = new ServiceBusMessage(msgContent);

            screensServiceBusSenderClient.sendMessage(msg);

            return ServiceResponse.builder()
                    .message("Message Sent")
                    .isSuccess(true)
                    .build();

        } catch (Exception e) {
            msgContent = "Message Not Delivered. Error Msg: " + e.getLocalizedMessage();
            return ServiceResponse.builder()
                    .message(msgContent)
                    .isSuccess(false)
                    .build();
        }

    }
}
