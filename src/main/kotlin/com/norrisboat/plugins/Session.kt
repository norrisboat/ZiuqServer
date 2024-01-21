package com.norrisboat.plugins

import com.norrisboat.data.models.session.ZiuqQuizSession
import io.ktor.server.application.*
import io.ktor.server.application.ApplicationCallPipeline.ApplicationPhase.Plugins
import io.ktor.server.sessions.*
import io.ktor.util.*

fun Application.configureSession() {
    install(Sessions) {
        cookie<ZiuqQuizSession>("SESSION")
    }

    intercept(Plugins) {
        if (call.sessions.get<ZiuqQuizSession>() == null) {
            val clientId = call.parameters["clientId"] ?: ""
            call.sessions.set(ZiuqQuizSession(clientId, generateNonce()))
        }
    }
}