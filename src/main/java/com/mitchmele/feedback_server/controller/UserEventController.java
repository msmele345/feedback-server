package com.mitchmele.feedback_server.controller;

import com.mitchmele.feedback_server.model.ServiceResponse;
import com.mitchmele.feedback_server.model.UserEventRequest;
import com.mitchmele.feedback_server.service.UserEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class UserEventController {

    private final UserEventService userEventService;

    @PostMapping("/status")
    public ResponseEntity<ServiceResponse> publishMessage(@RequestBody UserEventRequest request) {
        final ServiceResponse serviceResponse = userEventService.sendMessageToTopic(request);
        return ResponseEntity.ok(serviceResponse);
    }
}
