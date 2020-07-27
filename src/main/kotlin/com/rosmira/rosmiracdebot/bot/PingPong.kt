package com.rosmira.rosmiracdebot.bot

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class PingPong {
    @RequestMapping("/ping")
    fun ping() = "Pong"
}