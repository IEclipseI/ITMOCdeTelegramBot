package com.rosmira.rosmiracdebot.bot

import com.rosmira.rosmiracdebot.SpringApplicationContext
import com.rosmira.rosmiracdebot.bot.command.Signin
import org.apache.logging.log4j.kotlin.Logging
import org.telegram.telegrambots.bots.DefaultBotOptions
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.bots.AbsSender
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import kotlin.reflect.KClass

class RosmiraCdeBot(botOptions: DefaultBotOptions) : TelegramLongPollingCommandBot(botOptions), Logging {
    init {
        logger.info("Registering commands")
        registerCommand(Signin::class)
        registerDefaultAction(this::defaultCommand)
        logger.info("Commands registered")
    }

    private fun defaultCommand(absSender: AbsSender, message: Message) {
        logger.info("Received unknown command: ${message.text ?: "<No Args>"}")
        val sendMessage = SendMessage(message.chatId, "I don't know that command :(")
        try {
            absSender.execute(sendMessage)
        } catch (e: TelegramApiException) {
            logger.error("Can't send response: ${e.stackTrace}")
        }
    }

    private fun <T : BotCommand> registerCommand(kClass: KClass<T>) {
        logger.info("Registering command: ${kClass.simpleName}")
        val bean = SpringApplicationContext.getContext().getBean(kClass.java)
        register(bean)
        logger.info("Command registered: ${kClass.simpleName}")
    }

    override fun getBotUsername() = "RosmiraCdeBot"

    override fun getBotToken(): String = System.getenv("BOT_TOKEN")

    override fun processNonCommandUpdate(update: Update) {
        logger.info("Received update: $update")
        if (update.hasMessage() && update.message.hasText()) {
            val message = SendMessage()
                    .setChatId(update.message.chatId)
                    .setText(update.message.text)
            try {
                execute(message)
                logger.info("Answer sent for message: ${update.message.messageId}")
            } catch (e: TelegramApiException) {
                logger.error("Error on response for message: ${update.message.messageId}; ", e)
            }
        }
    }
}