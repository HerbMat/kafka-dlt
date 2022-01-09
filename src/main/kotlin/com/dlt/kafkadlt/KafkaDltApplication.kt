package com.dlt.kafkadlt

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KafkaDltApplication

fun main(args: Array<String>) {
    runApplication<KafkaDltApplication>(*args)
}
