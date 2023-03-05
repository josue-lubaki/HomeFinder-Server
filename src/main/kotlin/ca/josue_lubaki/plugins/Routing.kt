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

            // Auth routes
            route("auth/") {
                register()
                login()
            }

            route("api/v1/") {
                this.authenticate {
                    getSecretInfo()
                    authenticate()

                    route("admin") {
                        housesRoutes()
                        ownerRoutes()
                        addressRoutes()
                    }
                }
            }
        }
    }
}
