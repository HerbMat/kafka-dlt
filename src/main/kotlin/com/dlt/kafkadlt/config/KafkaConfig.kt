package com.dlt.kafkadlt.config

import org.apache.kafka.common.TopicPartition
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.kafka.annotation.KafkaListenerConfigurer
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.config.KafkaListenerEndpointRegistrar
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.listener.*
import org.springframework.kafka.retrytopic.DeadLetterPublishingRecovererFactory
import org.springframework.kafka.retrytopic.ListenerContainerFactoryConfigurer
import org.springframework.kafka.retrytopic.RetryTopicInternalBeanNames
import org.springframework.messaging.converter.MessageConversionException
import org.springframework.util.backoff.FixedBackOff
import java.time.Clock
import java.util.function.Consumer


@Configuration
class KafkaConfig {
    companion object {
        val logger = LoggerFactory.getLogger(javaClass)
    }

    @Bean
    fun errorHandler(): CommonLoggingErrorHandler {
        return CommonLoggingErrorHandler()
    }

    /**
     * Boot will autowire this into the container factory.
     */
    @Bean
    fun errorHandler(deadLetterPublishingRecoverer: DeadLetterPublishingRecoverer): DefaultErrorHandler {
        val errorHandler = DefaultErrorHandler(deadLetterPublishingRecoverer,  FixedBackOff(1000, 2))
        errorHandler.addNotRetryableExceptions(ListenerExecutionFailedException::class.java)

        return errorHandler
    }

    @Bean(name = ["retryableKafkaListenerContainerFactory"])
    fun retryableKafkaListenerContainerFactory(consumerFactory: ConsumerFactory<Any, Any>): ConcurrentKafkaListenerContainerFactory<*, *> {
        val factory =  ConcurrentKafkaListenerContainerFactory<Any, Any>();
        factory.consumerFactory = consumerFactory
        return factory;
    }


    @Bean
    fun publisher(bytesTemplate: KafkaTemplate<String, Int>): DeadLetterPublishingRecoverer {
        return DeadLetterPublishingRecoverer(bytesTemplate) { cr, e ->
            TopicPartition(
                cr.topic() + "-dlt",
                cr.partition()
            )
        }
    }
}