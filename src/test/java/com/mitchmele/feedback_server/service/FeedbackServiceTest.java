package com.mitchmele.feedback_server.service;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mitchmele.feedback_server.model.FeedbackRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.InputStream;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FeedbackServiceTest {

    @Mock
    private BlobContainerClient mockBlobContainerClient;

    @InjectMocks
    private FeedbackService feedbackService;


    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void uploadFeedback_usesContainerClientToUploadJson() {
        BlobClient mockBlob = Mockito.mock(BlobClient.class);

        when(mockBlobContainerClient.getBlobClient(any())).thenReturn(mockBlob);

        final FeedbackRequest request = FeedbackRequest.builder()
                .content("Great website")
                .email("user1@gmail.com")
                .timestamp(new Date().toString())
                .build();

        feedbackService.uploadFeedBack(request);

//        Mockito.verify(mockBlobContainerClient).up
    }
}