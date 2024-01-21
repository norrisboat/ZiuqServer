package com.norrisboat.data.models.liveQuiz

import com.norrisboat.data.models.question.QuestionResult
import kotlinx.serialization.Serializable


@Serializable
sealed interface LiveQuizConnection {

    @Serializable
    data class Join(val data: UserJoinQuiz) : LiveQuizConnection

    data class Ready(val quizReady: QuizReady) : LiveQuizConnection

    data class Answer(val quizAnswer: QuizAnswer) : LiveQuizConnection

    @Serializable
    object Nothing : LiveQuizConnection
}

@Serializable
sealed class LiveQuizAction {
    abstract val liveQuizActionType: LiveQuizActionType

    @Serializable
    data class PlayerJoined(
        val username: String,
        val name: String,
        val image: String,
        override val liveQuizActionType: LiveQuizActionType
    ) : LiveQuizAction()

    @Serializable
    data class WaitingForOpponent(override val liveQuizActionType: LiveQuizActionType = LiveQuizActionType.WAITING_FOR_OPPONENT) :
        LiveQuizAction()

    @Serializable
    data class Ready(
        override val liveQuizActionType: LiveQuizActionType = LiveQuizActionType.READY
    ) : LiveQuizAction()

    @Serializable
    data class ShowResults(
        override val liveQuizActionType: LiveQuizActionType
    ) : LiveQuizAction()

    @Serializable
    data class LoadingQuestions(override val liveQuizActionType: LiveQuizActionType = LiveQuizActionType.LOADING_QUESTIONS) :
        LiveQuizAction()

    @Serializable
    data class QuestionsReady(
        override val liveQuizActionType: LiveQuizActionType = LiveQuizActionType.QUESTIONS_READY,
        val questions: List<QuestionResult>
    ) :
        LiveQuizAction()

    @Serializable
    data class Start(override val liveQuizActionType: LiveQuizActionType = LiveQuizActionType.START) : LiveQuizAction()

    @Serializable
    data class Question(val questionIndex: Int, override val liveQuizActionType: LiveQuizActionType) : LiveQuizAction()

    @Serializable
    data class Score(
        val opponentScore: Int,
        val answer: String,
        override val liveQuizActionType: LiveQuizActionType
    ) :
        LiveQuizAction()

    @Serializable
    data class Results(
        val opponentScore: Int,
        override val liveQuizActionType: LiveQuizActionType = LiveQuizActionType.RESULTS
    ) :
        LiveQuizAction()
}

@Serializable
data class UserJoinQuiz(val userId: String, val liveQuizId: String, val name: String)

@Serializable
data class QuizReady(val category: String, val difficulty: String, val quizType: String, val liveQuizId: String)

@Serializable
data class QuizAnswer(val username: String, val answer: String, val score: Int, val liveQuizId: String)

@Serializable
data class QuizError(val errorMessage: String)