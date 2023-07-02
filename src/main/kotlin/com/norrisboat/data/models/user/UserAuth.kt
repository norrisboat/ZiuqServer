package com.norrisboat.data.models.user

import kotlinx.serialization.Serializable
import org.mindrot.jbcrypt.BCrypt

@Serializable
data class UserAuth(val username: String, val password: String) {
    fun hashedPassword(): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }

    fun correctPassword(pass: String): Boolean {
        return BCrypt.checkpw(password, pass)
    }
}