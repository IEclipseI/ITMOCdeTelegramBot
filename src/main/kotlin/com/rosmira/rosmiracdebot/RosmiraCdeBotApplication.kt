package com.rosmira.rosmiracdebot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class RosmiraCdeBotApplication

fun main(args: Array<String>) {
    val runApplication = runApplication<RosmiraCdeBotApplication>(*args)
}
