package com.dlt.kafkadlt.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.retrytopic.RetryTopicConfiguration;
import org.springframework.kafka.retrytopic.RetryTopicConfigurationBuilder;
import org.springframework.kafka.retrytopic.TopicSuffixingStrategy;

import static com.dlt.kafkadlt.utils.Constants.RETRYABLE_DEFAULT_TOPIC;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class RetryableKafkaConfig {

    private final KafkaProperties kafkaProperties;

    @Bean(name = "retryableKafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<Object, Object> retryableKafkaListenerContainerFactory(ConsumerFactory<Object, Object> consumerFactory) {
        var factory =  new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }

    @Bean(name = "retryableGlobalKafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<Object, Object> retryableGlobalKafkaListenerContainerFactory(ConsumerFactory<Object, Object> consumerFactory) {
        var factory =  new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }

    @Bean
    public RetryTopicConfiguration retryTopicConfigurer(KafkaTemplate<?, ?> kafkaTemplate, ConcurrentKafkaListenerContainerFactory<?, ?> retryableGlobalKafkaListenerContainerFactory) {
        return RetryTopicConfigurationBuilder.newInstance()
                .autoCreateTopicsWith(7, (short) 2)
                .fixedBackOff(3000)
                .maxAttempts(3)
                .listenerFactory(retryableGlobalKafkaListenerContainerFactory)
                .includeTopic(RETRYABLE_DEFAULT_TOPIC)
                .setTopicSuffixingStrategy(TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE)
                .retryTopicSuffix("-" + kafkaProperties.getConsumer().getGroupId() + "-retry")
                .dltSuffix("-" + kafkaProperties.getConsumer().getGroupId() + "-dlt")
                .create(kafkaTemplate);
    }
}