package com.norrisboat.data.models.liveQuiz

enum class LiveConnectionType {
    JOIN,
    DISCONNECT,
    READY,
    ANSWER,
    IDLE
}

enum class LiveQuizActionType {
    PLAYER_JOIN,
    WAITING_FOR_OPPONENT,
    READY,
    QUIZ_READY,
    LOADING_QUESTIONS,
    QUESTIONS_READY,
    START,
    QUESTION,
    SCORE,
    RESULTS,
    QUIZ_RESULTS,
    SHOW_RESULTS
}