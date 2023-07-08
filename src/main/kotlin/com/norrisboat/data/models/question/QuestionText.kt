package com.norrisboat.data.models.question


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuestionText(
    @SerialName("text")
    val text: String?
)