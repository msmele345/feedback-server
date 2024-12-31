package com.mitchmele.feedback_server.service;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mitchmele.feedback_server.model.FeedbackRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final BlobContainerClient blobContainerClient;

    private ObjectMapper objectMapper = new ObjectMapper();
    //publish to queue?
    //call http function

    public void uploadFeedBack(FeedbackRequest feedback) {
        feedback.setFeedbackId(UUID.randomUUID().toString());
        BlobClient blobClient = blobContainerClient.getBlobClient("user_feedback:" + feedback.getTimestamp());
        try {
            InputStream inputStream = new ByteArrayInputStream(objectMapper.writeValueAsBytes(feedback));
            blobClient.upload(inputStream);
        } catch (JsonProcessingException e) {
            log.info("Error while uploading feedback: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
