package com.mitchmele.feedback_server.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mitchmele.feedback_server.model.FeedbackRequest;
import com.mitchmele.feedback_server.model.ServiceResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FeedbackServiceTest {

    @Mock
    private RestTemplate feedbackRestTemplate;

    @Mock
    private UuidGenerator mockUuidGenerator;;

    @InjectMocks
    private FeedbackService feedbackService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    FeedbackRequest feedbackRequest;

    @BeforeEach
    void setUp() {
        String timestamp = new Date().toInstant().toString();
        feedbackRequest = FeedbackRequest.builder()
                .feedbackId("feedbackId")
                .content("Great website")
                .email("user1@gmail.com")
                .submissionTime(timestamp)
                .build();
        when(mockUuidGenerator.generateStringUuid()).thenReturn("someUuid");
    }

    @Test
    void uploadFeedback_usesContainerClientToUploadJson() {
        ServiceResponse expectedResponse = ServiceResponse.builder()
                .hasError(false)
                .status("Success")
                .build();

        when(feedbackRestTemplate.postForEntity(anyString(), any(), eq(Void.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResponseEntity<ServiceResponse> actual = feedbackService.uploadFeedBack(feedbackRequest);

        verify(mockUuidGenerator).generateStringUuid();
        verify(feedbackRestTemplate).postForEntity(anyString(), any(), eq(Void.class));


        assertThat(actual.getBody()).isEqualTo(expectedResponse);
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void uploadFeedback_returnsServiceResponseWithError_whenBlobContainerClientThrowsException() {
        RestClientResponseException ex =
                new RestClientResponseException("someerror", HttpStatusCode.valueOf(500), "500", null, null, null);

        when(feedbackRestTemplate.postForEntity(anyString(), any(), eq(Void.class)))
                .thenThrow(ex);

        ResponseEntity<ServiceResponse> actual = feedbackService.uploadFeedBack(feedbackRequest);

        ServiceResponse expectedResponse = ServiceResponse.builder()
                .hasError(true)
                .status("Upload failed. Error: someerror")
                .build();

        assertThat(actual.getBody()).isEqualTo(expectedResponse);
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

//        assertThatThrownBy(() -> feedbackService.uploadFeedBack(feedbackRequest))
//                .hasCauseExactlyInstanceOf(RuntimeException.class)
//                .hasMessage("Something went wrong");