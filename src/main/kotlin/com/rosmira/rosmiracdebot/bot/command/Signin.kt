package com.rosmira.rosmiracdebot.bot.command

import com.rosmira.rosmiracdebot.repo.UserRepository
import org.apache.logging.log4j.kotlin.Logging
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import javax.inject.Inject

class Signin : BotCommand("signin", ""), Logging {
    @Inject
    lateinit var userRepository: UserRepository

    override fun execute(absSender: AbsSender, user: User, chat: Chat, args: Array<String>) {
        val stringBuilder = StringBuilder()
        for ((index, value) in args.withIndex()) {
            stringBuilder.append("$index: $value, ")
        }
        logger.info("Received signin request: $stringBuilder")

        try {
            if (args.size < 2) {
                val notEnoughArgs = SendMessage()
                notEnoughArgs.text =
                        """Command requires two arguments:
                            |/signin LOGIN PASSWORD
                        """.trimMargin()
                notEnoughArgs.setChatId(chat.id)
                absSender.execute(notEnoughArgs)
            } else {
                val login = args[0]
                val password = args[1]
                userRepository
                        .save(com.rosmira.rosmiracdebot.model.User(login, password, "", user.id.toString()))
                val msg = SendMessage().setChatId(chat.id).setText("You signed in")
                absSender.execute(msg)
            }
        } catch (e: TelegramApiException) {
            logger.error("Cannot send response: ${e.stackTrace}")
        }
    }
}
