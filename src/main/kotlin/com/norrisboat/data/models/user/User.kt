package com.norrisboat.data.models.user

import com.norrisboat.data.tables.UserTable
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow

@Serializable
data class User(val id: String, val username: String, val password: String)

@Serializable
data class AuthUser(val id: String, val username: String)

fun User.toAuthUser() = AuthUser(this.id, this.username)

fun ResultRow.toUser(): User {
    return User(this[UserTable.id].toString(), this[UserTable.username], this[UserTable.password])
}