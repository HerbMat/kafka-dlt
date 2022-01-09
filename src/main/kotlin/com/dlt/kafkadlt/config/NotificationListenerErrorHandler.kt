package com.dlt.kafkadlt.config

import com.dlt.kafkadlt.model.Thing
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.header.Header
import org.apache.kafka.common.header.internals.RecordHeader
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.listener.KafkaListenerErrorHandler
import org.springframework.kafka.listener.ListenerExecutionFailedException
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.Message
import org.springframework.stereotype.Component
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

@Component("notificationListenerErrorHandler")
class NotificationListenerErrorHandler(private val kafkaTemplate: KafkaTemplate<Any, Any>): KafkaListenerErrorHandler {
    companion object {
        val logger = LoggerFactory.getLogger(javaClass)
    }

    override fun handleError(message: Message<*>, exception: ListenerExecutionFailedException): Any {
        logger.info(message.toString())
        logger.info(exception.toString())
        val producerRecord = ProducerRecord<Any, Any>(
            "${message.headers[KafkaHeaders.RECEIVED_TOPIC]}-dlt" as String,
            message.headers[KafkaHeaders.RECEIVED_PARTITION_ID] as Int,
            message.headers[KafkaHeaders.RECEIVED_MESSAGE_KEY],
            message.payload,
            message.headers.map { RecordHeader(it.key, toByteArray(it.value)) }
        )
        kafkaTemplate.send(producerRecord as ProducerRecord<Any, Any>)
       return Unit
    }

    private fun toByteArray(value: Any): ByteArray {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val objectOutputStream = ObjectOutputStream(byteArrayOutputStream)
        objectOutputStream.writeObject(this)
        objectOutputStream.flush()
        val result = byteArrayOutputStream.toByteArray()
        byteArrayOutputStream.close()
        objectOutputStream.close()
        return result
    }
}