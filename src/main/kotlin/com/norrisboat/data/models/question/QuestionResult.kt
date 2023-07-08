package com.norrisboat.data.models.question


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuestionResult(
    @SerialName("category")
    val category: String?,
    @SerialName("correctAnswer")
    val correctAnswer: String?,
    @SerialName("difficulty")
    val difficulty: String?,
    @SerialName("id")
    val id: String?,
    @SerialName("incorrectAnswers")
    val incorrectAnswers: List<String?>?,
    @SerialName("isNiche")
    val isNiche: Boolean?,
    @SerialName("question")
    val questionText: QuestionText?,
    @SerialName("regions")
    val regions: List<String?>?,
    @SerialName("tags")
    val tags: List<String?>?,
    @SerialName("type")
    val type: String?
)