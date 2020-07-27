package com.rosmira.rosmiracdebot.bot

import org.apache.logging.log4j.kotlin.Logging
import org.springframework.stereotype.Component
import org.telegram.telegrambots.ApiContextInitializer
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import javax.annotation.PostConstruct

@Component
class Deployer : Logging {
    @PostConstruct
    fun startCdeBot() {
        try {
            logger.info("App started");
            ApiContextInitializer.init();

            val botsApi = TelegramBotsApi();

            try {
                botsApi.registerBot(RosmiraCdeBot());
            } catch (e: TelegramApiException) {
                e.printStackTrace();
            }
            logger.info("App down")
        } catch (e: Exception) {
            logger.error("APP CRASHED")
        }
    }
}
