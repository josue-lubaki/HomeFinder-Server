package ca.josue_lubaki.routes

import ca.josue_lubaki.data.models.Role
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.util.pipeline.*

/**
 * @author Josue Lubaki
 * @version 1.0
 * @since 2023-03-05
 */

suspend fun PipelineContext<Unit, ApplicationCall>.isAdmin(): Boolean {
    val principal = call.authentication.principal<JWTPrincipal>()
    val role = principal?.payload?.getClaim("role")?.asString()

    return if (role == Role.ADMIN.name) {
        true
    } else {
        call.respond(HttpStatusCode.Forbidden, "You are not allowed to access this resource, only admins can do that")
        false
    }
}

suspend fun PipelineContext<Unit, ApplicationCall>.hasOwnerOrAdmin(): Boolean {
    val principal = call.authentication.principal<JWTPrincipal>()
    val role = principal?.payload?.getClaim("role")?.asString()
    val userId = principal?.payload?.getClaim("userId")?.asString()

    val id = call.parameters["id"] ?: return false

    return if (role == Role.ADMIN.name || userId == id) {
        true
    }
    else {
        call.respond(HttpStatusCode.Forbidden, "You are not allowed to access this resource, you can only access your own data")
        false
    }
}

suspend fun PipelineContext<Unit, ApplicationCall>.hasOwner(): Boolean {
    val principal = call.authentication.principal<JWTPrincipal>()
    val userId = principal?.payload?.getClaim("userId")?.asString()

    val id = call.parameters["id"] ?: return false

    return if (userId == id) {
        true
    }
    else {
        call.respond(HttpStatusCode.Forbidden, "You are not allowed to access this resource, you can only access your own data")
        false
    }
}

suspend fun PipelineContext<Unit, ApplicationCall>.isAuthenticated(): Boolean {
    val principal = call.authentication.principal<JWTPrincipal>()

    return if (principal != null) {
        true
    }
    else {
        call.respond(HttpStatusCode.Forbidden, "You are not allowed to access this resource, you need to be authenticated")
        false
    }
}