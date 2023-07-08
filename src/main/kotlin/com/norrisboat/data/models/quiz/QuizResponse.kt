package com.norrisboat.data.models.quiz

import com.norrisboat.data.models.question.Question
import kotlinx.serialization.Serializable

@Serializable
data class QuizResponse(
    val quizId: String,
    val questions: List<Question>,
    val results: String? = null,
    val createdAt: String? = null
)
