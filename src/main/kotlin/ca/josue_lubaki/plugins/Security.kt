package ca.josue_lubaki.plugins

import ca.josue_lubaki.data.models.Role
import ca.josue_lubaki.security.token.TokenConfig
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import org.koin.ktor.ext.inject

/**
 * @author Josue Lubaki
 * @version 1.0
 * @since 2023-03-04
 */

fun Application.configureSecurity() {

    val config : TokenConfig by inject()

    authentication {
        jwt {
            realm = this@configureSecurity.environment.config.property("jwt.realm").getString()

            verifier (
                JWT
                    .require(Algorithm.HMAC256(config.secret))
                    .withAudience(config.audience)
                    .withIssuer(config.issuer)
//                    .withClaim("role", Role.USER.name)
                    .build()
            )

            validate { credential ->
                if(credential.payload.audience.contains(config.audience)
                    && credential.payload.getClaim("role").asString() in listOf(Role.ADMIN.name, Role.USER.name)
                ) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
        }
    }
}