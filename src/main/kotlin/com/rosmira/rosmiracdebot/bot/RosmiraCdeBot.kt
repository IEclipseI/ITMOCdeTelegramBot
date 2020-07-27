package com.rosmira.rosmiracdebot.bot

import org.apache.logging.log4j.kotlin.Logging
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.exceptions.TelegramApiException

class RosmiraCdeBot: TelegramLongPollingBot(), Logging {
    override fun getBotUsername() = "RosmiraCdeBot"

    override fun getBotToken(): String = System.getenv("BOT_TOKEN")

    override fun onUpdateReceived(update: Update) {
        if (update.hasEditedMessage() && update.editedMessage.hasText()) {
            val sendMessage = SendMessage()
                    .setChatId(update.editedMessage.chatId)
                    .setText(update.editedMessage.text)
                    .setReplyToMessageId(update.editedMessage.messageId)
            try {
                execute(sendMessage)
                logger.debug("Caught for hand: " + update.message.messageId)
            } catch (e: TelegramApiException) {
                logger.error("Error on catching for hand: " + update.message.messageId)
                e.printStackTrace()
            }
        }
        if (update.hasMessage() && update.message.hasText()) {
            val message = SendMessage() // Create a SendMessage object with mandatory fields
                    .setChatId(update.message.chatId)
                    .setText(update.message.text)
            try {
                execute(message)
                logger.debug("Answer sent for message: " + update.message.messageId)
            } catch (e: TelegramApiException) {
                logger.error("Error on response for message: " + update.message.messageId)
                e.printStackTrace()
            }
        }
    }
}