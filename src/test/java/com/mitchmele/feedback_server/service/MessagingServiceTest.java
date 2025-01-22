package com.mitchmele.feedback_server.service;

import com.azure.messaging.servicebus.ServiceBusMessage;
import com.azure.messaging.servicebus.ServiceBusSenderClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mitchmele.feedback_server.model.UserEventRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MessagingServiceTest {

    @Mock
    private ServiceBusSenderClient mockSenderClient;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private MessagingService messagingService;


    @Test
    void shouldPublishUserEventWhenPayloadIsValid() throws JsonProcessingException {

       final String id = UUID.randomUUID().toString();
       final String mockTimestamp = "sometimestamp";

        UserEventRequest userEventRequest = UserEventRequest.builder()
                .id(id)
                .eventType("VIEWED")
                .imageName("2024_imageName")
                .timestamp(mockTimestamp)
                .build();

        String expectedMessageBody = "mockEventJson";
        ServiceBusMessage expectedMsg = new ServiceBusMessage(expectedMessageBody);

        when(objectMapper.writeValueAsString(Mockito.any())).thenReturn(expectedMessageBody);

        messagingService.sendMessageToTopic(userEventRequest);

        ArgumentCaptor<ServiceBusMessage> captor = ArgumentCaptor.forClass(ServiceBusMessage.class);

        Mockito.verify(mockSenderClient).sendMessage(captor.capture());

        assertThat(captor.getValue().getBody().toString()).isEqualTo(expectedMessageBody);
    }
}