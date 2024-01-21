package com.norrisboat.data.models.question

import com.norrisboat.data.tables.QuestionTable
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow

@Serializable
data class Question(
    val id: String,
    val questionId: String,
    val quizId: String,
    val createdAt: String,
    val questionResult: QuestionResult? = null
)

fun ResultRow.toQuestion(): Question {
    return Question(
        this[QuestionTable.id].toString(),
        this[QuestionTable.questionId],
        this[QuestionTable.quizId].toString(),
        this[QuestionTable.createdAt].toString()
    )
}