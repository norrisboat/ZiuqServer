package com.norrisboat.routes

import com.norrisboat.data.models.liveQuiz.*
import com.norrisboat.data.models.network.ErrorResponse
import com.norrisboat.data.models.network.Response
import com.norrisboat.data.models.quiz.QuizRequest
import com.norrisboat.data.models.session.ZiuqQuizSession
import com.norrisboat.data.models.user.LiveQuizUser
import com.norrisboat.services.LiveQuizService
import com.norrisboat.services.QuestionService
import com.norrisboat.utils.Routes.CREATE_LIVE_QUIZ
import com.norrisboat.utils.Routes.JOIN_LIVE_QUIZ
import com.norrisboat.utils.Routes.LIVE_QUIZ
import com.norrisboat.utils.Routes.QUIZ
import com.norrisboat.utils.SocketConnection
import com.norrisboat.utils.getRandomImage
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consumeEach
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonObject
import org.koin.ktor.ext.inject

fun Route.realtimeQuizRoutes() {

    val liveQuizService: LiveQuizService by inject()
    val socketConnection: SocketConnection by inject()
    val questionService: QuestionService by inject()

    route(QUIZ) {

        post(CREATE_LIVE_QUIZ) {

            val liveQuizRequest = try {
                call.receive<LiveQuizRequest>()
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, ErrorResponse(message = "Missing Some Fields"))
                return@post
            }

            try {
                val liveQuizCreate = liveQuizService.createLiveQuiz(liveQuizRequest)
                call.respond(HttpStatusCode.OK, Response(true, "Success", liveQuizCreate))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.Conflict, ErrorResponse(false, e.message ?: "Some Problem Occurred!"))
                return@post
            }
        }

        post(JOIN_LIVE_QUIZ) {
            val liveQuizJoinRequest = try {
                call.receive<LiveQuizJoinRequest>()
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, ErrorResponse(message = "Missing Some Fields"))
                return@post
            }

            try {
                val liveQuizJoinResponse = liveQuizService.joinLiveQuiz(liveQuizJoinRequest)
                if (!liveQuizJoinResponse.success) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        ErrorResponse(
                            message = liveQuizJoinResponse.errorMessage
                                ?: "Couldn't send quiz results. Please try again later"
                        )
                    )
                } else {
                    call.respond(HttpStatusCode.OK, Response(true, "Success", liveQuizJoinResponse))
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.Conflict, ErrorResponse(false, e.message ?: "Some Problem Occurred!"))
                return@post
            }
        }
    }

    route(QUIZ) {
        standardWebSocket { socket, userName, action ->
            when (action) {
                is LiveQuizConnection.Join -> {
                    val liveQuiz = socketConnection.liveQuizzes[action.data.liveQuizId]
                    if (liveQuiz == null) {
                        val quizError = QuizError("Room not found")
                        socket.send(Frame.Text(Json.encodeToString(quizError)))
                        return@standardWebSocket
                    }
                    val player = LiveQuizUser(action.data.userId, action.data.name, getRandomImage(), socket)
                    socketConnection.playerJoined(player)
                    if (liveQuiz.containsPlayer(player.username)) {
                        val playerInRoom = liveQuiz.players.find { it.username == player.username }
                        playerInRoom?.socket = socket
                    } else {
                        liveQuiz.addPlayer(player)
                    }
                }

                is LiveQuizConnection.Ready -> {
                    val liveQuiz = socketConnection.liveQuizzes[action.quizReady.liveQuizId]
                    if (liveQuiz == null) {
                        val quizError = QuizError("Room not found")
                        socket.send(Frame.Text(Json.encodeToString(quizError)))
                        return@standardWebSocket
                    }
                    liveQuiz.loadQuestions(
                        questionService,
                        QuizRequest(action.quizReady.category, action.quizReady.difficulty, action.quizReady.quizType)
                    )
                }

                is LiveQuizConnection.Answer -> {
                    val liveQuiz = socketConnection.liveQuizzes[action.quizAnswer.liveQuizId]
                    if (liveQuiz == null) {
                        val quizError = QuizError("Room not found")
                        socket.send(Frame.Text(Json.encodeToString(quizError)))
                        return@standardWebSocket
                    }
                    liveQuiz.playerAnswer(action.quizAnswer)
                }

                is LiveQuizConnection.Nothing -> {

                }
            }
        }
    }

}

fun Route.standardWebSocket(
    handleFrame: suspend (
        socket: DefaultWebSocketServerSession,
        userName: String,
        quizAction: LiveQuizConnection
    ) -> Unit
) {
    webSocket(LIVE_QUIZ) {
        val clientId = call.sessions.get<ZiuqQuizSession>()?.userId ?: kotlin.run {
            close(CloseReason(CloseReason.Codes.CANNOT_ACCEPT, "ClientId is null"))
            return@webSocket
        }
        println("clientId $clientId")

        try {
            incoming.consumeEach { frame ->
                if (frame is Frame.Text) {
                    val json = Json {
                        ignoreUnknownKeys = true
                    }
                    val frameTextReceived = frame.readText()
                    val jsonObject = json.parseToJsonElement(frameTextReceived).jsonObject

                    println("Norristest type:${jsonObject["type"].toString()}")
                    val quizAction = when (LiveConnectionType.values()
                        .firstOrNull { it.name.lowercase() == (jsonObject["type"] as JsonPrimitive).content }
                        ?: LiveConnectionType.IDLE) {
                        LiveConnectionType.JOIN -> LiveQuizConnection.Join(json.decodeFromString(frameTextReceived))
                        LiveConnectionType.READY -> LiveQuizConnection.Ready(json.decodeFromString(frameTextReceived))
                        LiveConnectionType.ANSWER -> LiveQuizConnection.Answer(json.decodeFromString(frameTextReceived))
                        LiveConnectionType.DISCONNECT -> {
                            close()
                            LiveQuizConnection.Nothing
                        }

                        LiveConnectionType.IDLE -> LiveQuizConnection.Nothing
                    }
                    handleFrame(this, clientId, quizAction)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            // Handle Socket Closed
        }
    }
}