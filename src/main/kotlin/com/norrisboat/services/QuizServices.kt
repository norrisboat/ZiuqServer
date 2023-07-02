package com.norrisboat.services

import com.norrisboat.data.models.quiz.Quiz
import com.norrisboat.data.models.quiz.toQuiz
import com.norrisboat.data.tables.QuizTable
import com.norrisboat.factory.DatabaseFactory.dbQuery
import com.norrisboat.utils.getUUID
import com.norrisboat.utils.toUUID
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.koin.core.component.KoinComponent
import java.util.*

interface QuizService {

    suspend fun getQuiz(id: String): Quiz?

    suspend fun getQuizForUser(userId: String): List<Quiz>

    suspend fun createQuiz(user: String): String

}

class QuizServiceImpl : QuizService, KoinComponent {

    override suspend fun getQuiz(id: String): Quiz? = dbQuery {
        return@dbQuery QuizTable.select { QuizTable.id eq id.toUUID() }.map { it.toQuiz() }.singleOrNull()
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

}