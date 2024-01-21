package com.norrisboat.data.models.liveQuiz

import com.norrisboat.data.models.question.QuestionResult
import com.norrisboat.data.models.quiz.QuizRequest
import com.norrisboat.data.models.user.LiveQuizUser
import com.norrisboat.services.QuestionService
import io.ktor.websocket.*
import kotlinx.coroutines.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class LiveQuiz(
    val liveQuizId: String,
    var players: List<LiveQuizUser> = listOf()
) {
    private var phase: LiveQuizAction = LiveQuizAction.WaitingForOpponent()
        set(value) {
            synchronized(field) {
                field = value
            }
        }

    private var questions: List<QuestionResult> = emptyList()
    private val questionAnswers: Array<Int> = arrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0)
    private var currentQuestionIndex = 0
    private var timerJob: Job? = null

    fun containsPlayer(username: String): Boolean {
        return players.find { it.username == username } != null
    }

    suspend fun addPlayer(liveQuizUser: LiveQuizUser) {
        println("Player joined ${liveQuizUser.username}")

        if (players.size == 2) {
            return
        }

        val tempList = players.toMutableList()
        tempList.add(liveQuizUser)
        players = tempList.toList()

        if (players.size == 1) {
            phase = LiveQuizAction.WaitingForOpponent()

            sendMessageTo(Json.encodeToString(phase), liveQuizUser.username)
        } else if (players.size == 2 && phase is LiveQuizAction.WaitingForOpponent) {

            sendPlayerJoined(players.first())
            sendPlayerJoined(players.last())
            sendToAll(
                Json.encodeToString(
                    LiveQuizAction.Ready(LiveQuizActionType.QUIZ_READY)
                )
            )
        }
    }

    private suspend fun sendPlayerJoined(player: LiveQuizUser) {
        val playerJoined = LiveQuizAction.PlayerJoined(
            player.username,
            player.name,
            player.pic,
            LiveQuizActionType.PLAYER_JOIN
        )
        sendToAll(Json.encodeToString(playerJoined))
        delay(1_000L)
    }

    private suspend fun sendMessageTo(message: String, username: String) {
        println("\nNorristest message -----> $message")
        val player = players.find { it.username == username }
        player?.socket?.send(Frame.Text(message))
    }

    private suspend fun sendToAllExcept(message: String, username: String) {
        players.forEach {
            if (it.username != username) {
                it.socket.send(Frame.Text(message))
            }
        }
    }

    private suspend fun sendToAll(message: String) {
        players.forEach {
            it.socket.send(Frame.Text(message))
        }
    }

    suspend fun loadQuestions(questionService: QuestionService, quizRequest: QuizRequest) {
        println("Player loadQuestion $quizRequest")
        phase = LiveQuizAction.LoadingQuestions()
        sendToAll(Json.encodeToString(phase))
        questions = questionService.fetchQuestions(quizRequest.category, quizRequest.difficulty, quizRequest.type)
        sendToAll(Json.encodeToString(LiveQuizAction.QuestionsReady(questions = questions)))
        delay(DELAY_GAME_START)
        phase = LiveQuizAction.Start()
        sendToAll(Json.encodeToString(phase))
        sendToAll(Json.encodeToString(LiveQuizAction.Question(currentQuestionIndex, LiveQuizActionType.QUESTION)))
        startCountDown()
    }

    suspend fun playerAnswer(quizAnswer: QuizAnswer) {
        println("playerAnswer $quizAnswer")
        val currentPlayer = players.single { it.username == quizAnswer.username }
        currentPlayer.score = quizAnswer.score
        questionAnswers[currentQuestionIndex] = questionAnswers[currentQuestionIndex] + 1

        sendMessageTo(
            Json.encodeToString(
                LiveQuizAction.Score(
                    quizAnswer.score,
                    quizAnswer.answer,
                    LiveQuizActionType.SCORE
                )
            ),
            otherPlayer(quizAnswer.username).username
        )
        if (questionAnswers[currentQuestionIndex] == 2 && currentQuestionIndex < 9) {
            timerJob?.cancel()
            nextQuestion()
        }

        if (currentQuestionIndex >= 9) {
            sendResults()
        }
    }

    private suspend fun sendResults() {
        sendMessageTo(
            Json.encodeToString(LiveQuizAction.Results(players.first().score, LiveQuizActionType.QUIZ_RESULTS)),
            otherPlayer(players.first().username).username
        )
        sendMessageTo(
            Json.encodeToString(LiveQuizAction.Results(players.last().score, LiveQuizActionType.QUIZ_RESULTS)),
            otherPlayer(players.last().username).username
        )
    }

    private fun otherPlayer(username: String) = players.single { it.username != username }

    @OptIn(DelicateCoroutinesApi::class)
    private fun startCountDown() {
        timerJob?.cancel()
        timerJob = GlobalScope.launch {
            var count = QUESTION_DURATION
            while (count >= 0f) {
                count--
                delay(1_000L)
            }

            if (count == -1 && currentQuestionIndex < 9) {
                nextQuestion()
            }

            if (currentQuestionIndex >= 9) {
                sendResults()
            }

        }
    }

    private suspend fun nextQuestion() {
        if (questionAnswers[currentQuestionIndex] != 0) {
            sendToAll(Json.encodeToString(LiveQuizAction.ShowResults(LiveQuizActionType.SHOW_RESULTS)))
        }
        delay(DELAY_NEXT_QUESTION)
        sendToAll(Json.encodeToString(LiveQuizAction.Question(++currentQuestionIndex, LiveQuizActionType.QUESTION)))
        startCountDown()
    }

    companion object {
        const val DELAY_GAME_START = 3000L
        const val DELAY_NEXT_QUESTION = 1500L
        const val QUESTION_DURATION = 15
    }
}
