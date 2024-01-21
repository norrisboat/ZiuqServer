package com.norrisboat.data.models.user

import com.norrisboat.data.tables.UserTable
import io.ktor.websocket.*
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow

@Serializable
data class User(val id: String, val username: String, val password: String)

@Serializable
data class AuthUser(val id: String, val username: String)

@Serializable
data class LiveQuizUser(
    val username: String,
    val name: String,
    val pic: String,
    var socket: WebSocketSession,
    var score: Int = 0
)

fun User.toAuthUser() = AuthUser(this.id, this.username)

fun ResultRow.toUser(): User {
    return User(this[UserTable.id].toString(), this[UserTable.username], this[UserTable.password])
}