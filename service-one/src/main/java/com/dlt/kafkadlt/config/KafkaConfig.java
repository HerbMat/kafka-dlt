package com.dlt.kafkadlt.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.annotation.KafkaListenerConfigurer;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerEndpointRegistrar;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.*;
import org.springframework.kafka.retrytopic.DeadLetterPublishingRecovererFactory;
import org.springframework.kafka.retrytopic.ListenerContainerFactoryConfigurer;
import org.springframework.kafka.retrytopic.RetryTopicInternalBeanNames;
import org.springframework.messaging.converter.MessageConversionException;
import org.springframework.util.backoff.FixedBackOff;
import java.time.Clock;
import java.util.function.Consumer;

@Slf4j
@Configuration
public class KafkaConfig {

//    @Bean
//    public CommonLoggingErrorHandler errorHandler()  {
//        return new CommonLoggingErrorHandler();
//    }

    /**
     * Boot will autowire this into the container factory.
     */
    @Bean
    public DefaultErrorHandler errorHandler(DeadLetterPublishingRecoverer deadLetterPublishingRecoverer)  {
        var errorHandler = new DefaultErrorHandler(deadLetterPublishingRecoverer,  new FixedBackOff(1000, 2));
//        errorHandler.addNotRetryableExceptions(ListenerExecutionFailedException.class);

        return errorHandler;
    }

    @Bean(name = "retryableKafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<?, ?> retryableKafkaListenerContainerFactory(ConsumerFactory<?, ?> consumerFactory) {
        var factory =  new ConcurrentKafkaListenerContainerFactory();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }


    @Bean
    public DeadLetterPublishingRecoverer publisher(KafkaTemplate<String, Integer> bytesTemplate) {
        return new DeadLetterPublishingRecoverer(bytesTemplate, ( cr, e ) -> new TopicPartition(
                cr.topic() + "-dlt",
                cr.partition()
        ));
    }
}