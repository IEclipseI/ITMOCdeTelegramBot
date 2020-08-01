package com.rosmira.rosmiracdebot.bot.command

import com.rosmira.rosmiracdebot.model.CdeUser
import com.rosmira.rosmiracdebot.service.CdeUserService
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import java.util.*
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

@Component
class Signin : BotCommand("signin", "<login> <password>"), Logging {
    @Autowired
    lateinit var cdeUserService: CdeUserService

    override fun execute(absSender: AbsSender, user: User, chat: Chat, args: Array<String>) {
        logger.info("Received signin request")

        try {
            if (args.size != 2) {
                val notEnoughArgs = SendMessage()
                notEnoughArgs.text =
                        """Command requires two arguments:
                            |/signin CDE_LOGIN CDE_PASSWORD
                        """.trimMargin()
                notEnoughArgs.setChatId(chat.id)
                absSender.execute(notEnoughArgs)
            } else {
                val login = args[0]
                val password = args[1]
                val tgUserId = user.id.toString()
                val responseMsg = if (checkSignin(login, password)) {
                    val secretKey = getSecretKey()
                    val instance = Cipher.getInstance("AES/ECB/PKCS5Padding")
                    instance.init(Cipher.ENCRYPT_MODE, secretKey)
                    val encryptedPas = instance.doFinal(password.toByteArray())
                    val encodedPassword = String(Base64.getEncoder().encode(encryptedPas))
                    cdeUserService
                            .save(CdeUser(login, encodedPassword, "", tgUserId))
                    SendMessage().setChatId(chat.id).setText(
                            """You signed in.
                            |Now you can use commands
                            """.trimMargin())
                } else {
                    SendMessage().setChatId(chat.id).setText("Invalid login or password")
                }
                absSender.execute(responseMsg)
            }
        } catch (e: TelegramApiException) {
            logger.error("Cannot send response for signin-command: ", e)
        }
    }

    private fun checkSignin(login: String, password: String): Boolean {
        return true
    }

    private fun getSecretKey(): SecretKey {
        val decodedSecretKey = System.getenv("SECRET_KEY")
        val secretKey = Base64.getDecoder().decode(decodedSecretKey)
        return SecretKeySpec(secretKey, "AES")
    }
}
