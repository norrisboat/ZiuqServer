package com.norrisboat.data.models.liveQuiz

import kotlinx.serialization.Serializable

@Serializable
data class LiveQuizRequest(val userId: String) {
    fun generateLiveId(): String {
        val validChars: List<Char> = ('A'..'Z') + ('0'..'9')
        return CharArray(6) { validChars.random() }.concatToString()
    }
}
