package com.rosmira.rosmiracdebot.bot.command

import org.apache.logging.log4j.kotlin.Logging
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender

class Login: BotCommand("login", "<login in CDE> <password>"), Logging {
    override fun execute(absSender: AbsSender, user: User, chat: Chat, strings: Array<String>) {
        val stringBuilder = StringBuilder()
        for ((index, value) in strings.withIndex()) {
            stringBuilder.append("$index: $value, ")
        }
        logger.info("Received login request: $stringBuilder")

        val msg = SendMessage().setChatId(chat.id).setText("You are logined")
        absSender.execute(msg)
    }
}