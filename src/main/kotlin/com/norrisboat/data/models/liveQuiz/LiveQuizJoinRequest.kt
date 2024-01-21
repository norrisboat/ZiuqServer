package com.norrisboat.data.models.liveQuiz

import kotlinx.serialization.Serializable

@Serializable
data class LiveQuizJoinRequest(val userId: String, val liveQuizId: String)
