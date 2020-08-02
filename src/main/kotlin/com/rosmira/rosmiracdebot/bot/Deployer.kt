package com.rosmira.rosmiracdebot.bot

import org.apache.logging.log4j.kotlin.Logging
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import org.telegram.telegrambots.ApiContextInitializer
import org.telegram.telegrambots.bots.DefaultBotOptions
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.meta.exceptions.TelegramApiException

@Component
class Deployer : Logging {
    @EventListener
    fun startApplication(event: ContextRefreshedEvent) {
        try {
            logger.info("App started")
            ApiContextInitializer.init()

            val botsApi = TelegramBotsApi()

            try {
                botsApi.registerBot(RosmiraCdeBot(DefaultBotOptions()))
            } catch (e: TelegramApiException) {
                logger.error("Cannot register bot: ", e)
            }
        } catch (e: Exception) {
            logger.error("APP CRASHED: ", e)
        }
    }
}
