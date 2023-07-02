package com.norrisboat

import com.norrisboat.factory.DatabaseFactory
import com.norrisboat.plugins.configureMonitoring
import com.norrisboat.plugins.configureRouting
import com.norrisboat.plugins.configureSerialization
import com.norrisboat.plugins.configureServices
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.koin.ktor.plugin.KoinApplicationStarted
import org.koin.ktor.plugin.KoinApplicationStopPreparing
import org.koin.ktor.plugin.KoinApplicationStopped

fun main() {
    embeddedServer(Netty, port = 9000, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureRouting()
    configureSerialization()
    configureMonitoring()
    configureDatabase()
    configureServices()
    main()

}

fun configureDatabase() {
    DatabaseFactory.connect()
}

fun Application.main() {
    // ...

    // Install Ktor features
    environment.monitor.subscribe(KoinApplicationStarted) {
        log.info("Koin started.")
    }

    environment.monitor.subscribe(KoinApplicationStopPreparing) {
        log.info("Koin stopping...")
    }

    environment.monitor.subscribe(KoinApplicationStopped) {
        log.info("Koin stopped.")
    }

    //...
}