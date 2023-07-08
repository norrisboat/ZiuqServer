package com.norrisboat.di

import com.norrisboat.services.*
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val servicesModule = module {
    single<AuthService> { AuthServiceImpl() }
    single<QuizService> { QuizServiceImpl() }
    single<QuestionService> { QuestionServiceImpl() }

    single {
        HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                })
            }

            install(Logging) {
                level = LogLevel.ALL
            }
        }
    }
}