package com.dlt.kafkadlt.service;

import com.dlt.kafkadlt.model.Thing;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.dlt.kafkadlt.utils.Constants.*;

@RequiredArgsConstructor
@Slf4j
@Service
public class ProducerService {
    private final KafkaTemplate<String, Thing> kafkaTemplate;

    public void sendMessage(String message) {
        log.info("Send to message normal topic $message");
        kafkaTemplate.send(TOPIC, UUID.randomUUID().toString(),  new Thing(message));
    }

    public void sendMessageGood(String message) {
        log.info("Send to message normal topic $message");
        kafkaTemplate.send(TOPIC_GOOD, UUID.randomUUID().toString(), new Thing(message));
    }

    public void sendMessageRepeatableTopic(String message) {
        log.info("Send message to repeatable topic $message");
        kafkaTemplate.send(REPEATABLE_TOPIC, UUID.randomUUID().toString(), new Thing(message));
    }
}