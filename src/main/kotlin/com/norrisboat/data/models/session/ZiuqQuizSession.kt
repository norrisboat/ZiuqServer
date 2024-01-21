package com.norrisboat.data.models.session

import kotlinx.serialization.Serializable

@Serializable
data class ZiuqQuizSession(
    val userId: String,
    val sessionId: String
)
