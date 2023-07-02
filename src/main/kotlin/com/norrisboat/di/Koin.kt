package com.norrisboat.di

import com.norrisboat.services.AuthService
import com.norrisboat.services.AuthServiceImpl
import com.norrisboat.services.QuizService
import com.norrisboat.services.QuizServiceImpl
import org.koin.dsl.module

val servicesModule = module {
    single<AuthService> { AuthServiceImpl() }
    single<QuizService> { QuizServiceImpl() }
}