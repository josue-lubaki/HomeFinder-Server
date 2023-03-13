package ca.josue_lubaki.routes

import ca.josue_lubaki.data.datasource.UserDataSource
import ca.josue_lubaki.data.models.ApiResponse
import ca.josue_lubaki.security.hashing.HashingService
import ca.josue_lubaki.tools.Utils.Companion.initPageAndLimit
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*
import org.koin.ktor.ext.inject
import kotlin.properties.Delegates

/**
 * @author Josue Lubaki
 * @version 1.0
 * @since 2023-03-12
 */

fun Route.userRoutes() {
    route("users") {
        val userDataSource: UserDataSource by inject()
        val hashingService : HashingService by inject()

        get {
            if(!isAdmin()) return@get
            val (page, limit) = initPageAndLimit()

            val users = userDataSource.getAllUsers(page, limit)
            val response = ApiResponse(
                success = users.success,
                message = users.message,
                prevPage = users.prevPage,
                nextPage = users.nextPage,
                data = users.data.map { it?.toDto()},
                lastUpdated = users.lastUpdated
            )
            call.respond(HttpStatusCode.OK, response)
        }

        get("/{id}") {
            if(!hasOwnerOrAdmin()) return@get
            val id = call.parameters["id"] ?: return@get
            val user = userDataSource.getUserById(id)
            val response = ApiResponse(
                success = user.success,
                message = user.message,
                data = listOf(user.data.first()?.toDto()),
                lastUpdated = user.lastUpdated
            )
            call.respond(HttpStatusCode.OK, response)
        }

        post {
            if(!isAdmin()) return@post
            registerUser(userDataSource, hashingService)
        }

        delete("/{id}") {
            if(!hasOwnerOrAdmin()) return@delete
            val id = call.parameters["id"] ?: return@delete
            userDataSource.deleteUser(id)
            call.respond(HttpStatusCode.NoContent)
        }
    }
}