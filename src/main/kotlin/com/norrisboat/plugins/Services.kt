package com.norrisboat.plugins

import com.norrisboat.di.servicesModule
import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin

fun Application.configureServices() {
    install(Koin) {
        modules(servicesModule)
    }
}
