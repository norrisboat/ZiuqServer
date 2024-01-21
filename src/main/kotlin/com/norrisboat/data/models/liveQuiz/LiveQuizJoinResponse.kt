package com.norrisboat.data.models.liveQuiz

import kotlinx.serialization.Serializable

@Serializable
data class LiveQuizJoinResponse(val success: Boolean, val errorMessage: String? = null)
