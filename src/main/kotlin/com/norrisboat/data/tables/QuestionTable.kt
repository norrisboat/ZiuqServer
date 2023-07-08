package com.norrisboat.data.tables

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object QuestionTable : Table(name = "questions") {
    val id = uuid("id")
    val questionId = text("question_id")
    val quizId = uuid("quiz_id") references QuizTable.id
    val createdAt = timestamp("created_at")

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}