package com.dlt.kafkadlt.service;

import com.dlt.kafkadlt.dto.Crown;
import com.dlt.kafkadlt.model.Info;
import com.dlt.kafkadlt.model.Thing;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void sendInvalidMessageToNormalTopic(String message) {
        log.info("Send to message {} with invalid format topic to topic {}", message, NORMAL_TOPIC);
        kafkaTemplate.send(NORMAL_TOPIC, UUID.randomUUID().toString(),  new Info(message));
    }

    public void sendValidMessageToNormalTopic(String message) {
        log.info("Send to valid message {} to failing topic {}", message, NORMAL_TOPIC);
        kafkaTemplate.send(NORMAL_TOPIC, UUID.randomUUID().toString(), new Thing(message));
    }

    public void sendValidMessageRetryableTopic(String message) {
        log.info("Send message {} with invalid format to retryable singe topic {}", message, RETRYABLE_SINGLE_TOPIC);
        kafkaTemplate.send(RETRYABLE_SINGLE_TOPIC, UUID.randomUUID().toString(), new Thing(message));
    }

    public void sendInvalidMessageRetryableTopic(String message) {
        log.info("Send message {} format to retryable singe topic {}", message, RETRYABLE_SINGLE_TOPIC);
        kafkaTemplate.send(RETRYABLE_SINGLE_TOPIC, UUID.randomUUID().toString(), new Info(message));
    }

    public void sendValidMessageToRetryableDefaultTopic(String message) {
        log.info("Send message {} to retryable default topic {}", message, RETRYABLE_DEFAULT_TOPIC);
        kafkaTemplate.send(RETRYABLE_DEFAULT_TOPIC, UUID.randomUUID().toString(), new Thing(message));
    }

    public void sendLast(Crown crown) {
        log.info("Send message {} to retryable default topic {}", crown.getText(), "last");
        try {
//            kafkaTemplate.send("last", UUID.randomUUID().toString(), "'value': 'is :badd'");
            kafkaTemplate.send("last", UUID.randomUUID().toString(), objectMapper.writer().writeValueAsString(crown));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}