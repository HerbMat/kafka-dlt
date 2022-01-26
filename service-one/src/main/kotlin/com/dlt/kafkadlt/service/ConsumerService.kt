package com.dlt.kafkadlt.service

import com.dlt.kafkadlt.model.Info
import com.dlt.kafkadlt.model.Thing
import com.dlt.kafkadlt.utils.REPEATABLE_TOPIC
import com.dlt.kafkadlt.utils.TOPIC
import com.dlt.kafkadlt.utils.TOPIC_GOOD
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.annotation.RetryableTopic
import org.springframework.kafka.retrytopic.TopicSuffixingStrategy
import org.springframework.retry.annotation.Backoff
import org.springframework.stereotype.Service
import java.lang.RuntimeException

@Service
class ConsumerService {
    companion object {
        val logger = LoggerFactory.getLogger(javaClass)

    }

    @KafkaListener(topics = [TOPIC], groupId = "group_id", errorHandler = "notificationListenerErrorHandler")
    fun consume(message: Info?) {
        logger.info("Got message $message")
        throw RuntimeException("Bad message")
    }

//    @KafkaListener(topics = [TOPIC_GOOD], groupId = "group_id", errorHandler = "notificationListenerErrorHandler")
    @KafkaListener(topics = [TOPIC_GOOD], groupId = "group_id")
    fun consumeGood(message: Thing?) {
        logger.info("Got message $message")
        throw RuntimeException("Bad message")
    }

    @RetryableTopic(
        attempts = "2",
        backoff = Backoff(delay = 1000, multiplier = 2.0),
        topicSuffixingStrategy = TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE,
        retryTopicSuffix = "group",
        dltTopicSuffix = "group-dlt",
        numPartitions = "7"
    )
    @KafkaListener(topics = [REPEATABLE_TOPIC], groupId = "group_id", containerFactory = "retryableKafkaListenerContainerFactory")
    fun consumeRepeatable(message: Thing?) {
        logger.info("Got message $message")
        throw RuntimeException("Bad message")
    }
}