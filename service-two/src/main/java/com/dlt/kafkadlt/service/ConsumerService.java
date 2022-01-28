package com.dlt.kafkadlt.service;

import com.dlt.kafkadlt.model.Info;
import com.dlt.kafkadlt.model.Thing;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;

import static com.dlt.kafkadlt.utils.Constants.*;

@Slf4j
@Service
public class ConsumerService {

    //    @KafkaListener(topics = TOPIC_GOOD, groupId = "group_id", errorHandler = "notificationListenerErrorHandler")
    @KafkaListener(topics = NORMAL_TOPIC)
    public void consumeNormalTopic(Thing message) {
        log.info("Got message {}", message);
    }

    @KafkaListener(topics = NORMAL_TOPIC, groupId = "third-group")
    public void consumeNormalTopicFail(Thing message) {
        log.info("Got message {}", message);
        throw new RuntimeException("Bad message");
    }

    @RetryableTopic(
            attempts = "3",
            backoff = @Backoff(delay = 1000, multiplier = 2.0),
            retryTopicSuffix = "#{'-' + '${spring.kafka.consumer.group-id}' + '-retry'}",
            dltTopicSuffix = "#{'-' + '${spring.kafka.consumer.group-id}' + '-dlt'}",
            numPartitions = "7"
    )
    @KafkaListener(topics = RETRYABLE_SINGLE_TOPIC, containerFactory = "retryableKafkaListenerContainerFactory")
//    @KafkaListener(topics = REPEATABLE_TOPIC)
    public void consumeRetryableSingle(Thing message) {
        log.info("Got message {}", message);
    }

    @KafkaListener(topics = RETRYABLE_DEFAULT_TOPIC, containerFactory = "retryableGlobalKafkaListenerContainerFactory")
    public void consumeRetryableDefault(Thing message) {
        log.info("Got message {}", message);
        throw new RuntimeException("Bad message");
    }
}