package com.norrisboat.routes

import com.norrisboat.data.models.network.ErrorResponse
import com.norrisboat.data.models.network.Response
import com.norrisboat.data.models.quiz.QuizRequest
import com.norrisboat.data.models.quiz.QuizResponse
import com.norrisboat.services.QuestionService
import com.norrisboat.services.QuizService
import com.norrisboat.utils.Routes.CREATE_QUIZ
import com.norrisboat.utils.Routes.GET_QUIZ
import com.norrisboat.utils.Routes.GET_USER_QUIZ
import com.norrisboat.utils.Routes.QUIZ
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.quizRoutes() {

    val quizService: QuizService by inject()
    val questionService: QuestionService by inject()

    route(QUIZ) {

        get(GET_QUIZ) {
            val quizId = try {
                call.parameters["quizId"].toString()
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, ErrorResponse(message = "Missing Some Fields"))
                return@get
            }

            call.respond(
                HttpStatusCode.OK,
                Response(true, "Success", quizService.getQuiz(quizId))
            )
        }

        get(GET_USER_QUIZ) {

            val userId = try {
                call.parameters["userId"].toString()
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, ErrorResponse(message = "Missing Some Fields"))
                return@get
            }

            try {
                val quizzes = quizService.getQuizForUser(userId)

                call.respond(HttpStatusCode.OK, Response(true, "", quizzes))

            } catch (e: Exception) {
                call.respond(HttpStatusCode.Conflict, ErrorResponse(false, e.message ?: "Some Problem Occurred!"))
                return@get
            }
        }

        post(CREATE_QUIZ) {

            val userId: String
            val quizRequest = try {
                userId = call.parameters["userId"].toString()
                call.receive<QuizRequest>()
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, ErrorResponse(message = "Missing Some Fields"))
                return@post
            }

            try {

                val quizId = quizService.createQuiz(userId)
                val questions = questionService.createQuizQuestions(quizId, quizRequest)

                if (questions.isEmpty()) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        ErrorResponse(message = "Couldn't create quiz. Try again later $questions")
                    )
                } else {
                    call.respond(HttpStatusCode.OK, Response(true, "Success", QuizResponse(quizId, questions)))
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.Conflict, ErrorResponse(false, e.message ?: "Some Problem Occurred!"))
                return@post
            }
        }
    }


}