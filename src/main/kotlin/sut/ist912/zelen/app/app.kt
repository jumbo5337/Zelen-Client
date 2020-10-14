package sut.ist912.zelen.app

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import sut.ist912m.zelen.app.entity.UserProfile


fun main() {
    val objectMapper = ObjectMapper().registerModule(KotlinModule())
    val source = """
        {
            "userId":14882281337,
            "username":"username",
            "registerTime":"2020-10-06T16:25:18.851Z",
            "lastSeen":"2020-10-14T11:01:25.489Z",
            "role":"USER",
            "firstName":"Фамилия",
            "lastName":"Фамилия",
            "email":"email@email.com",
            "balance":169.7
        }
        """.trimIndent()

    print(objectMapper.readValue(source, UserProfile::class.java))


}