package com.shimady.payment.processor.config;

import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
public interface KafkaConfiguration {

    @Container
    KafkaContainer KAFKA = new KafkaContainer(DockerImageName.parse("apache/kafka-native:4.4.0"));
}
