package com.mitchmele.feedback_server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class FeedbackServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(FeedbackServerApplication.class, args);
	}
}
