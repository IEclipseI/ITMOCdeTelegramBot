package com.rosmira.rosmiracdebot.bot

import org.apache.logging.log4j.kotlin.Logging
import org.springframework.stereotype.Component
import org.telegram.telegrambots.ApiContextInitializer
import org.telegram.telegrambots.bots.DefaultBotOptions
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import javax.annotation.PostConstruct

@Component
class Deployer : Logging {
    @PostConstruct
    fun startCdeBot() {
        try {
            logger.info("App started")
            ApiContextInitializer.init()

            val botsApi = TelegramBotsApi()

            try {
                botsApi.registerBot(RosmiraCdeBot(DefaultBotOptions()))
            } catch (e: TelegramApiException) {
                logger.error("Cannot register bot: ${e.stackTrace}")
            }
        } catch (e: Exception) {
            logger.error("APP CRASHED: ${e.stackTrace}")
        }
    }
}
