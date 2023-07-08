package com.norrisboat.services

import com.norrisboat.data.models.user.*
import com.norrisboat.data.tables.UserTable
import com.norrisboat.factory.DatabaseFactory.dbQuery
import com.norrisboat.utils.getUUID
import com.norrisboat.utils.toUUID
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.koin.core.component.KoinComponent

interface AuthService {

    fun getUser(id: String): User?
    fun getUserByUsername(name: String): User?

    suspend fun login(userAuth: UserAuth): AuthUser?

    suspend fun register(userAuth: UserAuth): AuthUser?

}

class AuthServiceImpl : AuthService, KoinComponent {

    override suspend fun login(userAuth: UserAuth): AuthUser? = dbQuery {
        val user = getUserByUsername(userAuth.username)
        user?.let {
            return@dbQuery if (userAuth.correctPassword(it.password)) {
                user.toAuthUser()
            } else {
                null
            }
        }
        return@dbQuery null
    }

    override suspend fun register(userAuth: UserAuth): AuthUser? = dbQuery {
        val uuid = UserTable.getUUID(UserTable.id)
        UserTable.insert { table ->
            table[id] = uuid
            table[password] = userAuth.hashedPassword()
            table[username] = userAuth.username
        }

        return@dbQuery getUser(uuid.toString())?.toAuthUser()
    }

    override fun getUser(id: String): User? {
        return UserTable.select { UserTable.id eq id.toUUID() }.map { it.toUser() }.singleOrNull()
    }

    override fun getUserByUsername(name: String): User? {
        return UserTable.select { UserTable.username eq name }.map { it.toUser() }.singleOrNull()
    }

}