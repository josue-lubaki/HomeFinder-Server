package ca.josue_lubaki.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

/**
 * @author Josue Lubaki
 * @version 1.0
 * @since 2023-03-04
 */

fun Application.configureSecurity(){
    authentication {
        jwt {
            realm = this@configureSecurity.environment.config.property("jwt.realm").getString()

            validate {
                UserIdPrincipal(it.payload.getClaim("username").asString())
            }
        }
    }
}