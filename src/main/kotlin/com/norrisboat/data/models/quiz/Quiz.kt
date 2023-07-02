package com.norrisboat.data.models.quiz

import com.norrisboat.data.tables.QuizTable
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow

@Serializable
data class Quiz(val id: String, val userId: String, val sessionId: String? = null, val results: String? = null)

fun ResultRow.toQuiz(): Quiz {
    return Quiz(
        this[QuizTable.id].toString(),
        this[QuizTable.userId].toString(),
        this[QuizTable.sessionId],
        this[QuizTable.results]
    )
}