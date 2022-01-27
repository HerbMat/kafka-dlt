package com.dlt.kafkadlt.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;

@Slf4j
@Configuration
public class AdditionalKafkaConfig {

    @Bean(name = "retryableKafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<Object, Object> retryableKafkaListenerContainerFactory(ConsumerFactory<Object, Object> consumerFactory) {
        var factory =  new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }
}