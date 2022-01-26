package com.dlt.kafkadlt.api

import com.dlt.kafkadlt.service.ProducerService
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class MessageController(private val producerService: ProducerService) {
    @PutMapping("/normal")
    fun sendMessage(@RequestParam message: String) {
        producerService.sendMessage(message)
    }

    @PutMapping("/good")
    fun sendMessageGood(@RequestParam message: String) {
        producerService.sendMessageGood(message)
    }

    @PutMapping("/repeatable")
    fun sendMessageRepeatable(@RequestParam message: String) {
        producerService.sendMessageRepeatableTopic(message)
    }
}