package com.mitchmele.feedback_server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mitchmele.feedback_server.model.FeedbackRequest;
import com.mitchmele.feedback_server.model.ServiceResponse;
import com.mitchmele.feedback_server.service.FeedbackService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class FeedbackControllerTest {

    @Mock
    private FeedbackService feedbackService;

    private FeedbackController feedbackController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;


    @BeforeEach
    void setUp() {
        feedbackController = new FeedbackController(feedbackService);
        mockMvc = MockMvcBuilders.standaloneSetup(feedbackController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void submitFeedback_ShouldPassRequestObjToServiceForUploading() throws Exception {
        final FeedbackRequest request = FeedbackRequest.builder()
                .content("Great website")
                .email("user1@gmail.com")
                .submissionTime(new Date().toString())
                .build();

        final ResponseEntity<ServiceResponse> expectedResponse = new ResponseEntity<>(
                new ServiceResponse(
                        "Success",
                        false
                ),
                HttpStatus.CREATED
        );

        Mockito.when(feedbackService.uploadFeedBack(any(FeedbackRequest.class)))
                .thenReturn(expectedResponse);

        mockMvc.perform(post("/api/v2/feedback")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated());

        Mockito.verify(feedbackService).uploadFeedBack(request);
    }
}