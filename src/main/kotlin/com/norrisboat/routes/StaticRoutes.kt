package com.norrisboat.routes

import io.ktor.server.http.content.*
import io.ktor.server.routing.*
import java.io.File

fun Route.staticRoutes() {
    staticFiles("/", File("Assets"))
}