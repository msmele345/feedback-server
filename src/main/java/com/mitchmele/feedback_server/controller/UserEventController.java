package com.mitchmele.feedback_server.controller;

import com.mitchmele.feedback_server.model.ServiceResponse;
import com.mitchmele.feedback_server.model.UserEventRequest;
import com.mitchmele.feedback_server.service.MessagingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class UserEventController {

    //publish to new topic
    //setup bus trigger function or http function that outputs to cosmos db.
    private final MessagingService messagingService;

    @PostMapping("/status")
    public ResponseEntity<ServiceResponse> publishMessage(@RequestBody UserEventRequest request) {
        final ServiceResponse serviceResponse = messagingService.sendMessageToTopic(request);
        return ResponseEntity.ok(serviceResponse);
    }
}
