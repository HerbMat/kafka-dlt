package com.dlt.kafkadlt.service

import com.dlt.kafkadlt.model.Thing
import com.dlt.kafkadlt.utils.REPEATABLE_TOPIC
import com.dlt.kafkadlt.utils.TOPIC
import com.dlt.kafkadlt.utils.TOPIC_GOOD
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import java.util.*

@Service
class ProducerService(private val kafkaTemplate: KafkaTemplate<String, Thing>) {
    companion object {
        val logger = LoggerFactory.getLogger(javaClass)
    }

    fun sendMessage(message: String) {
        logger.info("Send to message normal topic $message")
        kafkaTemplate.send(TOPIC, UUID.randomUUID().toString(),  Thing(message))
    }

    fun sendMessageGood(message: String) {
        logger.info("Send to message normal topic $message")
        kafkaTemplate.send(TOPIC_GOOD, UUID.randomUUID().toString(),  Thing(message))
    }

    fun sendMessageRepeatableTopic(message: String) {
        logger.info("Send message to repeatable topic $message")
        kafkaTemplate.send(REPEATABLE_TOPIC, UUID.randomUUID().toString(),  Thing(message))
    }
}