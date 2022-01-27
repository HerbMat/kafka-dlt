package com.dlt.kafkadlt.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.TopicPartition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.KafkaUtils;
import org.springframework.util.backoff.FixedBackOff;

@Slf4j
@Configuration
public class KafkaConfig {

    /**
     * Boot will autowire this into the container factory.
     */
    @Bean
    public DefaultErrorHandler errorHandler(DeadLetterPublishingRecoverer deadLetterPublishingRecoverer)  {
        return new DefaultErrorHandler(deadLetterPublishingRecoverer,  new FixedBackOff(1000, 2));
    }

    @Bean
    public DeadLetterPublishingRecoverer publisher(KafkaTemplate<String, Integer> bytesTemplate) {
        return new DeadLetterPublishingRecoverer(bytesTemplate, ( cr, e ) -> new TopicPartition(
                cr.topic() + "-" + KafkaUtils.getConsumerGroupId() + "-dlt",
                cr.partition()
        ));
    }
}