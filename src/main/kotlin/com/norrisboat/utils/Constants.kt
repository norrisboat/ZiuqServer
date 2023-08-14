package com.norrisboat.utils

object Routes {
    private const val VERSION = "v1"
    const val BASE_URL = "https://the-trivia-api.com"

    const val AUTH = "$VERSION/auth"
    const val LOGIN = "/login"
    const val REGISTER = "/register"

    const val QUIZ = "$VERSION/quiz"
    const val GET_QUIZ = "/{quizId}"
    const val GET_CATEGORIES = "/categories"
    const val GET_DIFFICULTIES = "/difficulties"
    const val GET_TYPES = "/types"
    const val GET_SETUP = "/setup"
    const val GET_USER_QUIZ = "/user/{userId}"
    const val CREATE_QUIZ = "/create/{userId}"
    const val UPDATE_QUIZ_RESULT = "/{quizId}/update"

}