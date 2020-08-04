package com.rosmira.rosmiracdebot.bot.util

import org.apache.http.Consts
import org.apache.http.HttpEntity
import org.apache.http.NameValuePair
import org.apache.http.client.config.RequestConfig
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.HttpPost
import org.apache.http.client.protocol.HttpClientContext
import org.apache.http.entity.ContentType
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import org.apache.http.message.BasicNameValuePair
import org.apache.http.util.EntityUtils
import java.util.*
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

class CdeUtil {
    companion object {
        val client: CloseableHttpClient = HttpClients.custom()
            .setDefaultRequestConfig(
                RequestConfig.custom()
                    .setConnectTimeout(2000)
                    .setConnectionRequestTimeout(2000)
                    .setSocketTimeout(2000)
                    .build())
            .build()

        const val SIGNIN_SERVLET = "https://de.ifmo.ru/servlet/"
        const val SHOWORKSPACE_PAGE = "https://de.ifmo.ru/servlet/distributedCDE?Rule=ShowWorkSpace"
        const val MARKS_PAGE = "https://de.ifmo.ru/servlet/distributedCDE?Rule=eRegister&"
        const val FORM_REGEX = "(<form id=\"FormName\">)([\\s\\S]*)(</form>)"
        const val ANTID_REGEX = "ANTID=[^\"]*"


        fun signin(login: String, password: String, context: HttpClientContext): Boolean {
            val signinForm = signinForm(login, password)

            val httpPost = HttpPost(SIGNIN_SERVLET)
            httpPost.entity = signinForm
            val logined = client.execute(httpPost, context)
            val content = logined.entity.body

            return !content.contains("Access is forbidden")
        }

        private fun signinForm(login: String, password: String): UrlEncodedFormEntity {
            val form: MutableList<NameValuePair> = ArrayList()
            form.add(BasicNameValuePair("Rule", "LOGON"))
            form.add(BasicNameValuePair("LOGIN", login))
            form.add(BasicNameValuePair("PASSWD", password))
            return UrlEncodedFormEntity(form, Consts.UTF_8)
        }

        fun decryptPas(password: String): String {
            val secretKey = getSecretKey()
            val decodeBase64 = Base64.getDecoder().decode(password)
            val instance = Cipher.getInstance("AES/ECB/PKCS5Padding")
            instance.init(Cipher.DECRYPT_MODE, secretKey)
            return String(instance.doFinal(decodeBase64))
        }

        private fun getSecretKey(): SecretKey {
            val decodedSecretKey = System.getenv("SECRET_KEY")
            val secretKey = Base64.getDecoder().decode(decodedSecretKey)
            return SecretKeySpec(secretKey, "AES")
        }

        fun encryptPassword(password: String): String {
            val secretKey = getSecretKey()
            val instance = Cipher.getInstance("AES/ECB/PKCS5Padding")
            instance.init(Cipher.ENCRYPT_MODE, secretKey)
            val encryptedPas = instance.doFinal(password.toByteArray())
            return String(Base64.getEncoder().encode(encryptedPas))
        }

        val HttpEntity.body: String
            get() {
                return EntityUtils.toString(this, ContentType.getOrDefault(this).charset)
            }
    }
}