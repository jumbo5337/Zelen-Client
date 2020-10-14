package sut.ist912.zelen.rest

import com.mashape.unirest.http.Unirest
import org.springframework.stereotype.Component
import sut.ist912.zelen.utils.Jwt
import sut.ist912m.zelen.app.dto.ZelenMessage
import sut.ist912m.zelen.app.entity.UserBalance
import sut.ist912m.zelen.app.entity.UserProfile

@Component
class UserClient : BaseClient() {


    fun loadUserProfile(): UserProfile {
        val response = Unirest.get("$API_URL/api/v1/user/profile")
                .header("Authorization", Jwt())
                .header("Content-Type", "application/json")
                .asString()
        return objectMapper.readValue(response.body, UserProfile::class.java)
    }

    fun resetSecret(newSecret: String): ZelenMessage {
        val response = Unirest.post("$API_URL/api/v1/user/update-secret")
                .header("Authorization",  Jwt())
                .header("Content-Type", "application/json")
                .body("{\r\n    \"secretCode\" : \"$newSecret\"\r\n}")
                .asString();
        return objectMapper.readValue(response.body, ZelenMessage::class.java)
    }

    fun changePassword(
            oldPassword: String,
            password1: String,
            password2: String
    ) : ZelenMessage {
        val response = Unirest.post("$API_URL/api/v1/user/change-password")
                .header("Authorization",  Jwt())
                .header("Content-Type", "application/json")
                .body("""
                    {
                            "password1" : "$password1",
                            "password2" : "$password2",
                            "oldPassword" : "$oldPassword"
                     }
                """.trimIndent())
                .asString();
        return objectMapper.readValue(response.body, ZelenMessage::class.java)
    }

    fun updateBalance(): UserBalance {
        val response = Unirest.get("$API_URL/api/v1/user/balance")
                .header("Authorization", Jwt())
                .header("Content-Type", "application/json")
                .asString()
        return objectMapper.readValue(response.body, UserBalance::class.java)
    }




}