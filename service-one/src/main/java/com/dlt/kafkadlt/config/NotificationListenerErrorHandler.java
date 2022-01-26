package com.dlt.kafkadlt.config;

import com.dlt.kafkadlt.model.Thing;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.KafkaListenerErrorHandler;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

@RequiredArgsConstructor
@Slf4j
@Component("notificationListenerErrorHandler")
public class NotificationListenerErrorHandler implements KafkaListenerErrorHandler {
    private final KafkaTemplate<?, ?> kafkaTemplate;

    @Override
    public Object handleError(Message<?> message, ListenerExecutionFailedException exception) {
        log.info(message.toString());
        log.info(exception.toString());
//        var producerRecord = new ProducerRecord<>(
//            "${message.headers[KafkaHeaders.RECEIVED_TOPIC]}-dlt" as String,
//            message.headers[KafkaHeaders.RECEIVED_PARTITION_ID] as Int,
//            message.headers[KafkaHeaders.RECEIVED_MESSAGE_KEY],
//            message.payload,
//            message.headers.map { RecordHeader(it.key, toByteArray(it.value)) }
//        )
//        kafkaTemplate.send(producerRecord as ProducerRecord<Any, Any>)
       return null;
    }

    private byte[] toByteArray(Object value) {
        var byteArrayOutputStream = new ByteArrayOutputStream();
        try (var objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
            objectOutputStream.writeObject(this);
            objectOutputStream.flush();
            var result = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.close();
            objectOutputStream.close();
            return result;
        } catch (IOException e) {
            throw new RuntimeException("Gone bad");
        }
    }
}