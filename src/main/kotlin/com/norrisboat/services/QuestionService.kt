package com.norrisboat.services

import com.norrisboat.data.models.question.Question
import com.norrisboat.data.models.question.QuestionResult
import com.norrisboat.data.models.question.toQuestion
import com.norrisboat.data.models.quiz.QuizRequest
import com.norrisboat.data.tables.QuestionTable
import com.norrisboat.factory.DatabaseFactory.dbQuery
import com.norrisboat.utils.Routes.BASE_URL
import com.norrisboat.utils.getUUID
import com.norrisboat.utils.toUUID
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface QuestionService {

    suspend fun getQuestions(ids: List<String>): List<QuestionResult>
    suspend fun getQuestion(id: String): QuestionResult?
    suspend fun getQuizQuestions(quizId: String): List<Question>
    suspend fun createQuizQuestions(quizId: String, quizRequest: QuizRequest): List<Question>
    suspend fun fetchQuestions(category: String, difficulty: String, type: String): List<QuestionResult>
    suspend fun insertQuestion(quizId: String, questionResult: QuestionResult): String
    suspend fun insertQuestions(quizId: String, questionResults: List<QuestionResult>)

}

class QuestionServiceImpl : QuestionService, KoinComponent {

    private val httpClient by inject<HttpClient>()
    override suspend fun getQuestions(ids: List<String>): List<QuestionResult> {
        val results = mutableListOf<QuestionResult>()
        ids.forEach { id ->
            getQuestion(id)?.let { question ->
                results.add(question)
            }
        }
        return results
    }

    override suspend fun getQuestion(id: String): QuestionResult? {
        val response = httpClient.get("${BASE_URL}/v2/question/$id")
        return if (response.status == HttpStatusCode.OK) {
            response.body()
        } else {
            null
        }
    }

    override suspend fun getQuizQuestions(quizId: String): List<Question> {
        val results = mutableListOf<Question>()

        dbQuery {
            val questions = QuestionTable.select { QuestionTable.quizId eq quizId.toUUID() }.map { it.toQuestion() }
            questions.forEach { question ->
                results.add(question.copy(questionResult = getQuestion(question.questionId)))
            }

        }

        return results
    }

    override suspend fun createQuizQuestions(quizId: String, quizRequest: QuizRequest): List<Question> {
        val questionResults = fetchQuestions(quizRequest.category, quizRequest.difficulty, quizRequest.type)
        insertQuestions(quizId, questionResults)
        return getQuizQuestions(quizId)
    }

    override suspend fun insertQuestion(quizId: String, questionResult: QuestionResult): String = dbQuery {
        val uuid = QuestionTable.getUUID(QuestionTable.id)
        QuestionTable.insert { table ->
            table[id] = uuid
            table[questionId] = questionResult.id ?: ""
            table[this.quizId] = quizId.toUUID()
        }

        return@dbQuery uuid.toString()
    }

    override suspend fun insertQuestions(quizId: String, questionResults: List<QuestionResult>) {
        questionResults.forEach { question ->
            insertQuestion(quizId, question)
        }
    }

    override suspend fun fetchQuestions(category: String, difficulty: String, type: String): List<QuestionResult> {
        val response =
            httpClient.get("${BASE_URL}/v2/questions?limit=10&categories=$category&difficulties=$difficulty&types=$type")
        return if (response.status == HttpStatusCode.OK) {
            response.body()
        } else {
            emptyList()
        }
    }

}