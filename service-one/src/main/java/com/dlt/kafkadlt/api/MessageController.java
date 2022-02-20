package com.dlt.kafkadlt.api;

import com.dlt.kafkadlt.dto.Crown;
import com.dlt.kafkadlt.service.ProducerService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
public class MessageController {
    private final ProducerService producerService;

    @Operation(summary = "Add message with invalid format to traditional listener")
    @PutMapping("/normal-invalid")
    void sendInvalidMessageToNormalTopic(@RequestParam String message) {
        producerService.sendInvalidMessageToNormalTopic(message);
    }

    @Operation(summary = "Add message to failing traditional listener.")
    @PutMapping("/normal")
    void sendMessageGood(@RequestParam String message) {
        producerService.sendValidMessageToNormalTopic(message);
    }

    @Operation(summary = "Add message to failing listener annotated with @RetryableTopic")
    @PutMapping("/retryable-single")
    void sendValidMessageRetryableTopic(@RequestParam String message) {
        producerService.sendValidMessageRetryableTopic(message);
    }

    @Operation(summary = "Add message with invalid format to listener annotated with @RetryableTopic")
    @PutMapping("/retryable-single-invalid")
    void sendInvalidMessageRetryableTopic(@RequestParam String message) {
        producerService.sendInvalidMessageRetryableTopic(message);
    }

    @Operation(summary = "Add message to failing listener annotated with default retryable configuration")
    @PutMapping("/retryable-default")
    void sendValidMessageToRetryableDefaultTopic(@RequestParam String message) {
        producerService.sendValidMessageToRetryableDefaultTopic(message);
    }

    @PutMapping("/last")
    void handleDefault(@RequestBody Crown crown) {
        producerService.sendLast(crown);
    }
}