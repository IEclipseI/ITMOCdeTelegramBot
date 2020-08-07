package com.rosmira.rosmiracdebot.bot.command

import com.rosmira.rosmiracdebot.bot.util.CdeUtil.Companion.FORM_REGEX
import com.rosmira.rosmiracdebot.bot.util.CdeUtil.Companion.MARKS_PAGE
import com.rosmira.rosmiracdebot.bot.util.CdeUtil.Companion.body
import com.rosmira.rosmiracdebot.bot.util.CdeUtil.Companion.client
import com.rosmira.rosmiracdebot.bot.util.CdeUtil.Companion.decryptPas
import com.rosmira.rosmiracdebot.bot.util.CdeUtil.Companion.signin
import com.rosmira.rosmiracdebot.service.CdeUserService
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.protocol.HttpClientContext
import org.apache.logging.log4j.kotlin.Logging
import org.jsoup.Jsoup
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
class GetScore : BotCommand("getmarks", ""), Logging {
    @Autowired
    lateinit var cdeUserService: CdeUserService

    override fun execute(absSender: AbsSender, user: User, chat: Chat, args: Array<String>) {
        logger.info("Received $commandIdentifier request")
        val cdeUser = cdeUserService.getCdeUserByTgUserId(user.id.toString())
        cdeUser?.also {
            try {
                val context = HttpClientContext.create()
                if (!signin(it.login, decryptPas(it.password), context)) {
                    val msg = SendMessage().setChatId(chat.id).setText(
                        """
                        Access is forbidden.
                        Possibly invalid login or password.
                        """.trimIndent())
                    absSender.execute(msg)
                } else {
                    val marksPage =
                        client.execute(HttpGet(MARKS_PAGE + it.antId), context)
                    val marks = parseMarks(marksPage.entity.body)
                    val msg =
                        SendMessage().setChatId(chat.id).setText(marks).setParseMode("Markdown")
                    absSender.execute(msg)
                }
            } catch (e: TelegramApiException) {
                logger.error(e, e)
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
                logger.error(e, e)
            }
        } ?: run {
            try {
                absSender.execute(SendMessage().setChatId(chat.id).setText(
                    """
                    To use commands you must sign in firstly:
                    /signin CDE_LOGIN CDE_PASSWORD
                    """.trimIndent()
                ))
            } catch (e: TelegramApiException) {
                logger.error("Cannot send response for $commandIdentifier-command: ", e)
            }
        }
    }

    fun parseMarks(body: String): String {
        val find = Regex(FORM_REGEX).find(body)?.value ?: ""
        val rows = Jsoup.parse(find).select("tr")
        val responseMsg = StringBuilder("`")
        var wasTerm = false
        for (row in rows) {
            if (row.getElementsContainingText("Семестр").isNotEmpty()) {
                row.getElementsContainingText("Семестр").firstOrNull()?.let {
                    if (wasTerm)
                        responseMsg.append('\n')
                    responseMsg.append(it.text() + "\n")
                    wasTerm = true
                }
            }
            if (row.getElementsByClass("td_vmenu_left").isNotEmpty()) {
                val replace =
                    row.child(2).text().replace("(\\([\\s\\S]*\\))".toRegex(), "")
                responseMsg
                    .append(String.format("%-30.30s", replace) + " – " + row.child(3).text() + "\n")
            }
        }
        return responseMsg.append('`').toString()
    }
}

