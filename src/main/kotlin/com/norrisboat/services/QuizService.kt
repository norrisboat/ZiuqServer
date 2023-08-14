package com.norrisboat.services

import com.norrisboat.data.models.quiz.Quiz
import com.norrisboat.data.models.quiz.QuizResponse
import com.norrisboat.data.models.quiz.toQuiz
import com.norrisboat.data.tables.QuizTable
import com.norrisboat.factory.DatabaseFactory.dbQuery
import com.norrisboat.utils.getUUID
import com.norrisboat.utils.toUUID
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.update
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*

interface QuizService {

    suspend fun getQuiz(id: String): QuizResponse?

    suspend fun getQuizForUser(userId: String): List<Quiz>

    suspend fun createQuiz(user: String): String

    suspend fun updateQuizResult(id: String, result: String): QuizResponse?

}

class QuizServiceImpl : QuizService, KoinComponent {

    private val questionService: QuestionService by inject()

    override suspend fun getQuiz(id: String): QuizResponse? {
        val quiz = dbQuery {
            QuizTable.select { QuizTable.id eq id.toUUID() }.map { it.toQuiz() }.singleOrNull()
        }
        quiz?.let {
            return QuizResponse(
                quizId = id,
                questions = questionService.getQuizQuestions(id),
                results = it.results,
                createdAt = it.createdAt
            )
        }
        return null
    }

    override suspend fun getQuizForUser(userId: String): List<Quiz> = dbQuery {
        return@dbQuery QuizTable.select { QuizTable.userId eq userId.toUUID() }.map { it.toQuiz() }
    }

    override suspend fun createQuiz(user: String): String = dbQuery {
        val uuid = QuizTable.getUUID(QuizTable.id)
        QuizTable.insert { table ->
            table[id] = uuid
            table[userId] = user.toUUID()
            table[sessionId] = UUID.randomUUID().toString()
        }

        return@dbQuery uuid.toString()
    }

    override suspend fun updateQuizResult(id: String, result: String): QuizResponse? = dbQuery {
        QuizTable.update({ QuizTable.id eq id.toUUID() }) { table ->
            table[results] = result
        }
        return@dbQuery getQuiz(id)
    }

}