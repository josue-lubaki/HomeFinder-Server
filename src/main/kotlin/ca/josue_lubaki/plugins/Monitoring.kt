package ca.josue_lubaki.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.request.*
import org.slf4j.event.Level

/**
 * @author Josue Lubaki
 * @version 1.0
 * @since 2023-03-04
 */

fun Application.configureMonitoring() {
    install(CallLogging){
        level = Level.DEBUG
        filter { call -> call.request.path().startsWith("/") }
    }
}