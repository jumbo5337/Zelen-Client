package sut.ist912.zelen.rest

import com.mashape.unirest.http.Unirest
import org.springframework.stereotype.Component
import sut.ist912.zelen.rest.dto.ZelenException


@Component
class LoginClient : BaseClient() {

    fun login(username: String, password: String) {
        val response = Unirest.post("$API_URL/api/v1/auth/token")
                .header("Content-Type", "application/json")
                .body("{\r\n    \"username\" : \"$username\",\r\n    \"password\" : \"$password\"\r\n}")
                .asJson()
        if (response.status == 200) {
            jwt = response.body.`object`["token"] as String
            println(jwt)
        } else {
            throw ZelenException(response.body.`object`["message"] as String)
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
    ){
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
                .asJson()
        if (response.status != 200) {
              throw ZelenException(response.body.`object`["message"] as String)
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
                .asJson()
        if (response.status != 200) {
            throw ZelenException(response.body.`object`["message"] as String)
        } else {
            return response.body.`object`["message"] as String
        }

    }


}