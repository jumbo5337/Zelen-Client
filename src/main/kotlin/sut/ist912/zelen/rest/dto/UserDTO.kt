package sut.ist912m.zelen.app.entity

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.time.Instant
import java.util.*

data class User (
        val id: Long,
        val username: String,
        val password: String,
        val registerTime : Instant,
        val lastSeen: Instant,
        val secret: String,
        val role: Role
)

data class UserInfo(
        val userId : Long,
        val firstName : String,
        val lastName: String,
        val email: String
)

data class UserBalance(
        val userId : Long,
        val balance: Double
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class UserProfile (
        val userId: Long,
        val username: String,
        val registerTime : Date,
        val lastSeen: Date,
        val role: String,
        val firstName : String,
        val lastName: String,
        val email: String,
        val balance: Double
)