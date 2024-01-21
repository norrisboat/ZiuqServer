package com.norrisboat.utils

import com.norrisboat.data.models.liveQuiz.LiveQuiz
import com.norrisboat.data.models.user.LiveQuizUser
import java.util.concurrent.ConcurrentHashMap

class SocketConnection {

    val liveQuizzes = ConcurrentHashMap<String, LiveQuiz>()

    val players = ConcurrentHashMap<String, LiveQuizUser>()

    fun playerJoined(player: LiveQuizUser) {
        players[player.username] = player
    }

    fun getRoomWithClientId(clientId: String): LiveQuiz? {
        val filteredRooms = liveQuizzes.filterValues { room ->
            room.players.find { player ->
                player.username == clientId
            } != null
        }
        return if (filteredRooms.values.isEmpty()) {
            null
        } else {
            filteredRooms.values.toList()[0]
        }
    }
}