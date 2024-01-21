package com.norrisboat.data.models.liveQuiz

import kotlinx.serialization.Serializable

@Serializable
data class LiveQuizCreate(val userId: String, val liveQuizId: String)
