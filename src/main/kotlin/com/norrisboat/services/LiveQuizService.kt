package com.norrisboat.services

import com.norrisboat.data.models.liveQuiz.*
import com.norrisboat.utils.SocketConnection
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface LiveQuizService {
    fun createLiveQuiz(liveQuizRequest: LiveQuizRequest): LiveQuizCreate
    fun joinLiveQuiz(liveQuizJoinRequest: LiveQuizJoinRequest): LiveQuizJoinResponse
}

class LiveQuizServiceImpl : LiveQuizService, KoinComponent {

    private val socketConnection by inject<SocketConnection>()

    override fun createLiveQuiz(liveQuizRequest: LiveQuizRequest): LiveQuizCreate {
        var liveQuizId = ""
        while (liveQuizId.isBlank()) {
            val generatedId = liveQuizRequest.generateLiveId()
            if (socketConnection.liveQuizzes[generatedId] == null) {
                liveQuizId = generatedId
            }
        }

        val liveQuiz = LiveQuiz(liveQuizId = liveQuizId)
        socketConnection.liveQuizzes[liveQuizId] = liveQuiz

        return LiveQuizCreate(liveQuizRequest.userId, liveQuizId)
    }

    override fun joinLiveQuiz(liveQuizJoinRequest: LiveQuizJoinRequest): LiveQuizJoinResponse {
        val room = socketConnection.liveQuizzes[liveQuizJoinRequest.liveQuizId]

        return when {
            room == null -> {
                LiveQuizJoinResponse(success = false, errorMessage = "Quiz not found")
            }

            room.containsPlayer(liveQuizJoinRequest.userId) -> {
                LiveQuizJoinResponse(success = false, errorMessage = "A Player with this name has already joined")
            }

            room.players.size >= 2 -> {
                LiveQuizJoinResponse(success = false, errorMessage = "Quiz already full")
            }

            else -> {
                LiveQuizJoinResponse(success = true)
            }
        }

    }
}