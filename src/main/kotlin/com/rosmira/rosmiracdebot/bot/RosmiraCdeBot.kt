package com.rosmira.rosmiracdebot.bot

import com.rosmira.rosmiracdebot.bot.command.Signin
import org.apache.logging.log4j.kotlin.Logging
import org.telegram.telegrambots.bots.DefaultBotOptions
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.exceptions.TelegramApiException

class RosmiraCdeBot(botOptions: DefaultBotOptions) : TelegramLongPollingCommandBot(botOptions), Logging {
    init {
        logger.info("Registering commands")
        register(Signin())
        logger.info("Commands registered")
    }

    override fun getBotUsername() = "RosmiraCdeBot"

    override fun getBotToken(): String = System.getenv("BOT_TOKEN")

    override fun processNonCommandUpdate(update: Update) {
        logger.info("Received update: $update")
        if (update.hasEditedMessage() && update.editedMessage.hasText()) {
            val sendMessage = SendMessage()
                    .setChatId(update.editedMessage.chatId)
                    .setText(update.editedMessage.text)
                    .setReplyToMessageId(update.editedMessage.messageId)
            try {
                execute(sendMessage)
                logger.info("Caught for hand: ${update.editedMessage.messageId}")
            } catch (e: TelegramApiException) {
                logger.error("Error on catching for hand: ${update.editedMessage.messageId}")
                e.printStackTrace()
            }
        }
        if (update.hasMessage() && update.message.hasText()) {
            val message = SendMessage()
                    .setChatId(update.message.chatId)
                    .setText(update.message.text)
            try {
                execute(message)
                logger.info("Answer sent for message: ${update.message.messageId}")
            } catch (e: TelegramApiException) {
                logger.error("Error on response for message: ${update.message.messageId}")
                e.printStackTrace()
            }
        }
    }
}