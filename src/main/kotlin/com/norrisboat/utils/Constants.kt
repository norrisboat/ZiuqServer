package com.norrisboat.utils

object Routes {
    private const val VERSION = "v1"

    const val AUTH = "$VERSION/auth"
    const val LOGIN = "/login"
    const val REGISTER = "/register"

    const val QUIZ = "$VERSION/quiz"
    const val GET_QUIZ = "/{quizId}"
    const val GET_USER_QUIZ = "/user/{userId}"
    const val CREATE_QUIZ = "/create/{userId}"

}