package com.norrisboat.data.tables

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object UserTable : Table(name = "user") {
    val id = uuid("id")
    val username = varchar("username", 255)
    val password = text("password")
    val createdAt = timestamp("created_at")

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}