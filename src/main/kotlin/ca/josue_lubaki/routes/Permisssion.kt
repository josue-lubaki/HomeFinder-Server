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

    if (role == Role.ADMIN.name) { return true }

    call.respond(HttpStatusCode.Forbidden, "You are not allowed to access this resource, only admins can do that")
    return false
}

suspend fun PipelineContext<Unit, ApplicationCall>.isAuthenticated(): Boolean {
    val principal = call.authentication.principal<JWTPrincipal>()

    if (principal != null) { return true }

    call.respond(HttpStatusCode.Forbidden, "You are not allowed to access this resource, you need to be authenticated")
    return false
}