package com.norrisboat.data.models.quiz


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuizResultUpdate(
    @SerialName("result")
    val result: String
)