package com.mitchmele.feedback_server.config;

import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class StorageBlobConfig {

    @Bean
    public BlobContainerClient blobContainerClient() {
        BlobContainerClientBuilder containerBuilder = new BlobContainerClientBuilder();
        return containerBuilder.containerName("feedback")
                .connectionString("")
                .containerName("feedback")
                .buildClient();

    }
}
