package com.norrisboat.plugins

import com.norrisboat.di.servicesModule
import com.norrisboat.di.socketConnectionModule
import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin

fun Application.configureServices() {
    install(Koin) {
        modules(servicesModule, socketConnectionModule)
    }
}
