package ca.josue_lubaki.plugins

import ca.josue_lubaki.routes.authenticate
import ca.josue_lubaki.routes.getSecretInfo
import ca.josue_lubaki.routes.login
import ca.josue_lubaki.routes.register
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        register()
        login()
        authenticate()
        getSecretInfo()
    }
}
