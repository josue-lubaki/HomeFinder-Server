package ca.josue_lubaki.plugins

import ca.josue_lubaki.routes.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {

        route("/"){
            get {
                call.respondText("Welcome to HomeFinder App !")
            }

            // WhiteListed Routes
            // http://localhost:8080/auth/register
            route("auth/") {
                register()
                login()
            }

            // User Routes
            // http://localhost:8080/api/v1/
            route("api/v1/") {
                this.authenticate {
                    getSecretInfo()
                    authenticate()
                    housesRoutes()
                    ownerRoutes()
                    addressRoutes()
                }
            }
        }
    }
}
