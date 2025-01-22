package com.mitchmele.feedback_server.config;

import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusProcessorClient;
import com.azure.messaging.servicebus.ServiceBusReceiverClient;
import com.azure.messaging.servicebus.ServiceBusSenderClient;
import com.azure.messaging.servicebus.models.ServiceBusReceiveMode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration(proxyBeanMethods = false)
public class ServiceBusProcessorClientConfig {
    //connection strings omitted, will be added to key vault
    @Value("${messaging.servicebus.connection-string}")
    private String connectionString;

    @Bean
    public ServiceBusSenderClient screensServiceBusSenderClient() {
        ServiceBusClientBuilder busClientBuilder = new ServiceBusClientBuilder();
        return busClientBuilder
                .connectionString(connectionString)
                .sender()
                .topicName("bustopic2932")
                .buildClient();
    }
}

