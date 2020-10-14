package sut.ist912.zelen.rest

import com.mashape.unirest.http.Unirest
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties
import org.springframework.stereotype.Component
import sut.ist912.zelen.rest.dto.ZelenException
import sut.ist912m.zelen.app.dto.ZelenMessage
import java.lang.RuntimeException


@Component
class LoginClient : BaseClient() {

    fun login(username: String, password: String) {
        val response = Unirest.post("$API_URL/api/v1/auth/token")
                .header("Content-Type", "application/json")
                .body("{\r\n    \"username\" : \"$username\",\r\n    \"password\" : \"$password\"\r\n}")
                .asJson()
        if (response.status == 200) {
            jwtToken = response.body.`object`.getString("token")
        } else {
            throw ZelenException(response.body.`object`.getString("message"))
        }
    }

    fun register(
            username: String,
            password1: String,
            password2: String,
            secret: String,
            firstName: String,
            lastName: String,
            email: String,
    ) {
        val response = Unirest.post("$API_URL/api/v1/auth/register")
                .header("Content-Type", "application/json")
                .body("""
                    {
                         "username" : "$username",
                         "password1" : "$password1",
                         "password2" : "$password2",
                         "secretCode" : "$secret",
                         "firstName": "$firstName",
                         "secondName" : "$lastName",
                         "email" : "$email" 
                     }
                """.trimIndent())
                .asString()
        if (response.status != 200) {
            val msg = objectMapper.readValue(response.body, ZelenMessage::class.java)
            throw ZelenException(msg.message)
        }

    }

    fun resetPassword(
            username: String,
            password1: String,
            password2: String,
            secret: String,
    ): String {
        val response = Unirest.post("$API_URL/api/v1/auth/reset-password")
                .header("Content-Type", "application/json")
                .body("""
                    {
                            "password1" : "$password1",
                            "password2" : "$password2",
                            "secretCode" : "$secret",
                            "username" : "$username" 
                     }
                """.trimIndent())
                .asString()

        when (response.status) {
            200 -> return objectMapper.readValue(response.body, ZelenMessage::class.java).message
            400 -> {
                val msg = objectMapper.readValue(response.body, ZelenMessage::class.java)
                throw ZelenException(msg.message)
            }
            else -> throw RuntimeException(response.body)
        }
    }

    companion object {
        lateinit var jwtToken: String
    }


}