package com.norrisboat.data.models.quiz

import kotlinx.serialization.Serializable

@Serializable
data class QuizRequest(val category: String, val difficulty: String, val type: String)
