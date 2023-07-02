package com.norrisboat.plugins

import com.norrisboat.routes.authRoutes
import com.norrisboat.routes.quizRoutes
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*

fun Application.configureRouting() {

    routing {
        authRoutes()
        quizRoutes()
        // Static plugin. Try to access `/static/index.html`
        static("/static") {
            resources("static")
        }
    }
}
