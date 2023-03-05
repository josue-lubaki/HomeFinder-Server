package ca.josue_lubaki

import ca.josue_lubaki.plugins.*
import io.ktor.server.application.*

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused")
fun Application.module() {

    configureKoin()
    configureSerialization()
    configureMonitoring()
    configureSecurity()
    configureRouting()

}
