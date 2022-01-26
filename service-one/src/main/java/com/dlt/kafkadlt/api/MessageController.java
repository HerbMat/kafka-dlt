package com.dlt.kafkadlt.api;

import com.dlt.kafkadlt.service.ProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
public class MessageController {
    private final ProducerService producerService;
    @PutMapping("/normal")
    void sendMessage(@RequestParam String message) {
        producerService.sendMessage(message);
    }

    @PutMapping("/good")
    void sendMessageGood(@RequestParam String message) {
        producerService.sendMessageGood(message);
    }

    @PutMapping("/repeatable")
    void sendMessageRepeatable(@RequestParam String message) {
        producerService.sendMessageRepeatableTopic(message);
    }
}