package com.rosmira.rosmiracdebot.control

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class PingPong {
    @RequestMapping("/ping")
    fun ping() = "Pong"
}