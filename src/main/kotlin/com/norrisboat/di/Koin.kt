package com.norrisboat.di

import com.norrisboat.services.AuthService
import com.norrisboat.services.AuthServiceImpl
import org.koin.dsl.module

val servicesModule = module {
    single<AuthService> { AuthServiceImpl() }
}