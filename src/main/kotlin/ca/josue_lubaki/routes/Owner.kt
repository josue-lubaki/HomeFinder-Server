package ca.josue_lubaki.routes

import ca.josue_lubaki.data.datasource.OwnerDataSource
import ca.josue_lubaki.data.request.owner.OwnerRequest
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.bson.types.ObjectId
import org.koin.ktor.ext.inject

/**
 * @author Josue Lubaki
 * @version 1.0
 * @since 2023-03-05
 */

fun Route.ownerRoutes() {
    route("owners"){
        val ownerDataSource : OwnerDataSource by inject()

        // Create a new owner [POST]
        // http://localhost:8080/api/v1/owners
        post {
            if (!isAdmin()) return@post

            val request = kotlin.runCatching { call.receiveNullable<OwnerRequest>() }.getOrNull() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest, "Invalid request")
                return@post
            }

            // verify if the owner already exists
            val owner = ownerDataSource.getOwnerByUsername(request.username)
            if (owner != null){
                call.respond(HttpStatusCode.Conflict, "This owner already exists")
                return@post
            }

            val ownerRequest = request.toOwner()
            val newOwner = ownerDataSource.insertOwner(ownerRequest)
            if(newOwner){ call.respond(HttpStatusCode.Created, "Owner created") }
            else { call.respond(HttpStatusCode.InternalServerError, "An error occurred") }
        }

        // Get all owners [GET]
        // http://localhost:8080/api/v1/owners
        get {
            val owners = ownerDataSource.getAllOwners()
            call.respond(owners)
        }

        // Get a specific owner [GET]
        // http://localhost:8080/api/v1/owners/{id}
        get("/{id}"){
            val id = call.parameters["id"] ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest, "Invalid id")
                return@get
            }
            val owner = ownerDataSource.getOwnerById(id)
            if (owner != null){
                call.respond(HttpStatusCode.OK, owner)
            } else {
                call.respond(HttpStatusCode.NotFound, "Owner not found")
            }
        }

        // Update a specific owner [PUT]
        // http://localhost:8080/api/v1/owners/{id}
        put("/{id}"){
            if (!isAdmin()) return@put
            val id = call.parameters["id"] ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest, "Invalid id")
                return@put
            }
            val request = kotlin.runCatching { call.receiveNullable<OwnerRequest>() }.getOrNull() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest, "Invalid request")
                return@put
            }

            val ownerRequest = request.toOwner().copy(id = ObjectId(id))
            val updatedOwner = ownerDataSource.updateOwner(ownerRequest)
            if(updatedOwner){
                call.respond(HttpStatusCode.OK, "Owner updated")
            } else {
                call.respond(HttpStatusCode.InternalServerError, "An error occurred")
            }
        }

        // Delete a specific owner [DELETE]
        // http://localhost:8080/api/v1/owners/{id}
        delete("/{id}"){
            if (!isAdmin()) return@delete
            val id = call.parameters["id"] ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest, "Invalid id")
                return@delete
            }
            ObjectId.isValid(id)

            val deletedOwner = ownerDataSource.deleteOwner(id)
            if(deletedOwner){ call.respond(HttpStatusCode.OK, "Owner deleted") }
            else { call.respond(HttpStatusCode.InternalServerError, "An error occurred") }
        }
    }
}

