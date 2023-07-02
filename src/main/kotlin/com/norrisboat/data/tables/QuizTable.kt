package com.norrisboat.data.tables

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object QuizTable : Table(name = "quiz") {
    val id = uuid("id")
    val sessionId = text("session_id").nullable()
    val results = text("results").nullable()
    val userId = uuid("user_id") references UserTable.id
    val createdAt = timestamp("created_at")

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}