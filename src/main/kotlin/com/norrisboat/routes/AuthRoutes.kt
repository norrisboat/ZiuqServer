package com.norrisboat.routes

import com.norrisboat.data.models.network.ErrorResponse
import com.norrisboat.data.models.network.Response
import com.norrisboat.data.models.user.UserAuth
import com.norrisboat.services.AuthService
import com.norrisboat.utils.Routes.AUTH
import com.norrisboat.utils.Routes.LOGIN
import com.norrisboat.utils.Routes.REGISTER
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.authRoutes() {

    val authService: AuthService by inject()

    route(AUTH) {

        post(REGISTER) {

            val userAuth = try {
                call.receive<UserAuth>()
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, ErrorResponse(message = "Missing Some Fields"))
                return@post
            }

            try {
                val user = authService.register(userAuth)

                if (user == null) {
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse(message = "Failed to create account"))
                } else {
                    call.respond(HttpStatusCode.OK, Response(true, "Account created", user))
                }

            } catch (e: Exception) {
                call.respond(HttpStatusCode.Conflict, ErrorResponse(false, e.message ?: "Some Problem Occurred!"))
                return@post
            }
        }

        post(LOGIN) {

            val userAuth = try {
                call.receive<UserAuth>()
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, ErrorResponse(message = "Missing Some Fields"))
                return@post
            }

            try {
                val user = authService.login(userAuth)

                if (user == null) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        ErrorResponse(message = "Incorrect username or password")
                    )
                } else {
                    call.respond(HttpStatusCode.OK, Response(true, "Login successful", user))
                }

            } catch (e: Exception) {
                call.respond(HttpStatusCode.Conflict, ErrorResponse(false, e.message ?: "Some Problem Occurred!"))
                return@post
            }
        }
    }


}