package com.rosmira.rosmiracdebot.bot.command

import org.apache.logging.log4j.kotlin.Logging
import org.springframework.stereotype.Component
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender
import org.telegram.telegrambots.meta.exceptions.TelegramApiException

@Component
class Help : BotCommand("help", ""), Logging {
    override fun execute(absSender: AbsSender, user: User, chat: Chat, args: Array<String>) {
        logger.info("Received $commandIdentifier request")
        try {
            val msg = SendMessage().setChatId(chat.id).setText(
                """
                /signin CDE_LOGIN CDE_PASSWORD — ваши логин/пароль от ЦДО. Для использования команд достаточно ввести один раз.
                /getmarks — получить свои баллы за текущий учебный год.
                /help — показать этот текст.
                """.trimIndent())
            absSender.execute(msg)
        } catch (e: TelegramApiException) {
            logger.error(e, e)
        }
    }
}

