package ca.josue_lubaki.routes

import ca.josue_lubaki.data.datasource.UserDataSource
import ca.josue_lubaki.data.models.User
import ca.josue_lubaki.data.request.auth.AuthRequest
import ca.josue_lubaki.data.request.auth.RegisterRequest
import ca.josue_lubaki.data.response.auth.AuthResponse
import ca.josue_lubaki.security.hashing.HashingService
import ca.josue_lubaki.security.hashing.SaltedHash
import ca.josue_lubaki.security.token.TokenClaim
import ca.josue_lubaki.security.token.TokenConfig
import ca.josue_lubaki.security.token.TokenService
import ca.josue_lubaki.tools.Utils
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

/**
 * @author Josue Lubaki
 * @version 1.0
 * @since 2023-03-04
 */

fun Route.register() {
    val hashingService : HashingService by inject()
    val userDataSource : UserDataSource by inject()

    post("register") {
        val request = kotlin.runCatching { call.receiveNullable<RegisterRequest>() }.getOrNull() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest,"Invalid request")
            return@post
        }

        val areFieldsBlank = Utils.requestRegisterValidator(request)
        val isPasswordTooShort = request.password.length < 8

        if(areFieldsBlank || isPasswordTooShort) {
            call.respond(HttpStatusCode.Conflict)
            return@post
        }

        val userExists = userDataSource.getUserByUsername(request.username)
        if(userExists != null) {
            call.respond(HttpStatusCode.Conflict,"User already exists")
            return@post
        }

        val saltedHash = hashingService.generateSaltedHash(request.password)
        val user = User (
            username = request.username,
            password = saltedHash.hash,
            salt = saltedHash.salt,
            email = request.email,
            firstName = request.firstName,
            lastName = request.lastName
        )

        val wasAcknowledged = userDataSource.insertUser(user)
        if(!wasAcknowledged) {
            call.respond(HttpStatusCode.Conflict)
            return@post
        }

        call.respond(HttpStatusCode.Created)
    }
}

fun Route.login() {
    val hashingService : HashingService by inject()
    val userDataSource : UserDataSource by inject()
    val tokenService : TokenService by inject()
    val tokenConfig : TokenConfig by inject()

    // Authentification route
    // http://localhost:8080/auth/login
    post("login") {
        val request = kotlin.runCatching { call.receiveNullable<AuthRequest>() }.getOrNull() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest, "Invalid request")
            return@post
        }

        val user = userDataSource.getUserByUsername(request.username)
        if(user == null) {
            call.respond(HttpStatusCode.NotFound)
            return@post
        }

        val isPasswordCorrect = hashingService.verify(
            password = request.password,
            saltedHash = SaltedHash(
                hash = user.password,
                salt = user.salt
            )
        )
        if(!isPasswordCorrect) {
            call.respond (HttpStatusCode.Conflict, "Incorrect username or password")
            return@post
        }

        val token = tokenService.generateToken(
            config = tokenConfig,
            claims = arrayOf (
                TokenClaim (
                    name = "userId",
                    value = user.id.toString()
                ),
                TokenClaim (
                    name = "role",
                    value = user.role.name
                )
            )
        )

        call.respond (HttpStatusCode.OK, AuthResponse(token))
    }
}

// Check if the user is authenticated
// http://localhost:8080/auth/authenticate
fun Route.authenticate() {
    authenticate {
        get("authenticate"){ call.respond(HttpStatusCode.OK) }
    }
}

// Get the secret info of the user
// http://localhost:8080/auth/secret
fun Route.getSecretInfo() {
    authenticate {
        get("secret") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.payload?.getClaim("userId")?.asString()
            val role = principal?.payload?.getClaim("role")?.asString()

            call.respond(
                status = HttpStatusCode.OK,
                message= "User id: $userId, role: $role",
            )
        }
    }
}
