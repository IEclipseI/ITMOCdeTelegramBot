package com.rosmira.rosmiracdebot.bot.command

import com.rosmira.rosmiracdebot.service.CdeUserService
import org.apache.http.Consts
import org.apache.http.HttpEntity
import org.apache.http.NameValuePair
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.client.protocol.HttpClientContext
import org.apache.http.entity.ContentType
import org.apache.http.impl.client.HttpClients
import org.apache.http.message.BasicNameValuePair
import org.apache.http.util.EntityUtils
import org.apache.logging.log4j.kotlin.Logging
import org.jsoup.Jsoup
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender
import java.util.*
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec
import kotlin.collections.ArrayList

@Component
class GetScore : BotCommand("getmarks", ""), Logging {
    @Autowired
    lateinit var cdeUserService: CdeUserService

    override fun execute(absSender: AbsSender, user: User, chat: Chat, args: Array<String>) {
        logger.info("Received getscore request")
        val cdeUser = cdeUserService.getCdeUserByTgUserId(user.id.toString())
        cdeUser ?. let {
            val context = HttpClientContext.create()
            HttpClients.createDefault()?.use {
                val signin = HttpGet("https://de.ifmo.ru/?node=signin")
                it.execute(signin, context)

                val form: MutableList<NameValuePair> = ArrayList()
                form.add(BasicNameValuePair("Rule", "LOGON"))
                form.add(BasicNameValuePair("LOGIN", cdeUser.login))
                form.add(BasicNameValuePair("PASSWD", decryptPas(cdeUser.password)))
                val formEntity = UrlEncodedFormEntity(form, Consts.UTF_8)

                val httpPost = HttpPost("https://de.ifmo.ru/servlet/")
                httpPost.entity = formEntity
                val logined = it.execute(httpPost, context);
                val entity = logined.entity;

                val content = entity.body
                if (content.contains("Access is forbidden")) {
                    println("""
                    Access is forbidden.
                    Possibly invalid login or password.
                """.trimIndent())
                } else {

                    val workspace =
                            it.execute(HttpGet("https://de.ifmo.ru/servlet/distributedCDE?Rule=ShowWorkSpace"));
                    val workspacePage = workspace.entity.body
                    val antId: String =
                            Regex("ANTID=[^\"]*").find(workspacePage)?.value ?: ""

                    if (antId.isNotEmpty()) {
                        val marks =
                                it.execute(HttpGet("https://de.ifmo.ru/servlet/distributedCDE?Rule=eRegister&$antId"))
                        val body = marks.entity.body
                        val find = Regex("(<form id=\"FormName\">)([\\s\\S]*)(</form>)").find(body)?.value ?: ""
                        val rows = Jsoup.parse(find).select("tr")
                        val responseMsg = StringBuilder()
                        for (row in rows) {
                            if (row.getElementsByClass("td_vmenu_left").isNotEmpty()) {
                                val replace =
                                        row.child(2).text().replace("(\\([\\s\\S]*\\))".toRegex(), "")
                                responseMsg.append(String.format("%-30.30s", replace) + " – " + row.child(3).text() + "\n")
                            }
                        }
                        val msg = SendMessage().setChatId(chat.id).setText(responseMsg.toString())
                        absSender.execute(msg)
                    } else {
                        print("Something goes wrong")
                    }
                    val exit = HttpGet("https://de.ifmo.ru/servlet/distributedCDE?Rule=SYSTEM_EXIT");
                    it.execute(exit, context);
                }
            }
        }
    }

    private fun decryptPas(password: String): String {
        val decodeBase64 = Base64.getDecoder().decode(password)
        val secretKey = getSecretKey()
        val instance = Cipher.getInstance("AES/ECB/PKCS5Padding")
        instance.init(Cipher.DECRYPT_MODE, secretKey)
        return String(instance.doFinal(decodeBase64))
    }

    private fun getSecretKey(): SecretKey {
        val decodedSecretKey = System.getenv("SECRET_KEY")
        val secretKey = Base64.getDecoder().decode(decodedSecretKey)
        return SecretKeySpec(secretKey, "AES")
    }

    private val HttpEntity.body: String
        get() {
            return EntityUtils.toString(this, ContentType.getOrDefault(this).charset)
        }

}
