package com.rosmira.rosmiracdebot.bot.command

import com.rosmira.rosmiracdebot.repo.CdeUserRepository
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender
import org.telegram.telegrambots.meta.exceptions.TelegramApiException

@Component
class Signin: BotCommand("signin", "<login> <password>"), Logging {
    @Autowired
    lateinit var cdeUserRepository: CdeUserRepository

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
                cdeUserRepository
                        .save(com.rosmira.rosmiracdebot.model.CdeUser(login, password, "", user.id.toString() + login))
                val msg = SendMessage().setChatId(chat.id).setText("You signed in")
                absSender.execute(msg)
            }
        } catch (e: TelegramApiException) {
            logger.error("Cannot send response: ${e.stackTrace}")
        }
    }
}
