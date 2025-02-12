package com.mitchmele.feedback_server.integration;

import com.azure.core.util.BinaryData;
import com.azure.messaging.servicebus.ServiceBusMessage;
import com.azure.messaging.servicebus.ServiceBusSenderClient;
import com.mitchmele.feedback_server.model.FeedbackRequest;
import com.mitchmele.feedback_server.model.ServiceResponse;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("IT")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FeedbackIntegrationTest {

    @LocalServerPort
    private int port;

    @MockitoBean
    private ServiceBusSenderClient mockTopicSenderClient;

    private TestRestTemplate restTemplate = new TestRestTemplate();

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void contextLoads() {

    }

    @Test
    void feedbackProcessingEndToEndAndReturn201CreatedAfterPublishingToTopic() throws Exception {
        Mockito.doNothing().when(mockTopicSenderClient).sendMessage(Mockito.any());

        final FeedbackRequest request = FeedbackRequest.builder()
                .content("Great website")
                .email("user1@gmail.com")
                .submissionTime(new Date().toString())
                .build();

        ResponseEntity<ServiceResponse> actualRes = restTemplate.postForEntity(
                createUrlWithPath("/api/v2/feedback"),
                request,
                ServiceResponse.class);

        ArgumentCaptor<ServiceBusMessage> captor = ArgumentCaptor.forClass(ServiceBusMessage.class);

        Mockito.verify(mockTopicSenderClient).sendMessage(captor.capture());

        BinaryData msgBody = captor.getValue().getBody();

        assertThat(msgBody).isNotNull();

        FeedbackRequest sentMessage = objectMapper.readValue(msgBody.toString(), FeedbackRequest.class);

        assertThat(sentMessage.getContent()).isEqualTo(request.getContent());
        assertThat(sentMessage.getEmail()).isEqualTo(request.getEmail());
        assertThat(sentMessage.getSubmissionTime()).isEqualTo(request.getSubmissionTime());

        assertThat(actualRes.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(actualRes.getBody()).isNotNull();
        assertThat(actualRes.getBody().getMessage()).isEqualTo("Processed Feedback Successfully");

    }

    private String createUrlWithPath(String path) {
        return "http://localhost:" + port + path;
    }
}
