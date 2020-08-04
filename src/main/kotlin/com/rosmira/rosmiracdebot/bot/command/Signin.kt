package com.rosmira.rosmiracdebot.bot.command

import com.rosmira.rosmiracdebot.bot.util.CdeUtil
import com.rosmira.rosmiracdebot.bot.util.CdeUtil.Companion.body
import com.rosmira.rosmiracdebot.bot.util.CdeUtil.Companion.client
import com.rosmira.rosmiracdebot.bot.util.CdeUtil.Companion.encryptPassword
import com.rosmira.rosmiracdebot.bot.util.CdeUtil.Companion.signin
import com.rosmira.rosmiracdebot.model.CdeUser
import com.rosmira.rosmiracdebot.service.CdeUserService
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.protocol.HttpClientContext
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import java.io.IOException

@Component
class Signin : BotCommand("signin", "<login> <password>"), Logging {
    @Autowired
    lateinit var cdeUserService: CdeUserService

    override fun execute(absSender: AbsSender, user: User, chat: Chat, args: Array<String>) {
        logger.info("Received $commandIdentifier request")

        try {
            if (args.size != 2) {
                val notEnoughArgs = SendMessage().setChatId(chat.id).setText(
                    """
                    Command requires two arguments:
                    /signin CDE_LOGIN CDE_PASSWORD
                    """.trimIndent()
                )
                absSender.execute(notEnoughArgs)
            } else {
                val login = args[0]
                val password = args[1]
                val tgUserId = user.id.toString()
                val context = HttpClientContext.create()

                val responseMsg = if (signin(login, password, context)) {
                    val workspacePage = client.execute(HttpGet(CdeUtil.SHOWORKSPACE_PAGE), context).entity.body
                    val antId: String = Regex(CdeUtil.ANTID_REGEX).find(workspacePage)?.value ?: ""

                    val encryptedPassword = encryptPassword(password)

                    cdeUserService.save(CdeUser(login, encryptedPassword, antId, tgUserId))

                    SendMessage().setChatId(chat.id).setText(
                        """
                        You signed in.
                        Now you can use commands.
                        """.trimIndent()
                    )
                } else {
                    SendMessage().setChatId(chat.id).setText("Possibly invalid login or password")
                }
                absSender.execute(responseMsg)
            }
        } catch (e: TelegramApiException) {
            logger.error("Cannot send response for $commandIdentifier-command: ", e)
        } catch (e: IOException) {
            try {
                absSender.execute(SendMessage().setChatId(chat.id).setText(
                    """
                    Something goes wrong.
                    Please try later.
                    """.trimIndent()))

            } catch (f: TelegramApiException) {
                logger.error("Cannot send response for $commandIdentifier-command: ", f)
            }
            logger.error(e)
        }
    }
}
