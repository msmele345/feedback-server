package com.mitchmele.feedback_server.config;

import lombok.RequiredArgsConstructor;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
@RequiredArgsConstructor
public class HttpConfig {
    //for rest client to call azure function
    private final RestTemplateBuilder restTemplateBuilder;

    @Bean
    public RestTemplate feedbackRestTemplate() {
        return restTemplateBuilder
                .requestFactory(this::clientHttpRequestFactory)
                .build();
    }

    @Bean
    public HttpComponentsClientHttpRequestFactory clientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpRequestFactory.setHttpClient(httpClient());
        httpRequestFactory.setConnectTimeout(Duration.ofSeconds(5));
        return httpRequestFactory;

    }

    @Bean
    public CloseableHttpClient httpClient() {
        return HttpClientBuilder.create().build();
    }
}
