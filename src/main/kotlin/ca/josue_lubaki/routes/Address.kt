package ca.josue_lubaki.routes

import ca.josue_lubaki.data.datasource.AddressDataSource
import ca.josue_lubaki.data.request.address.AddressRequest
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

fun Route.addressRoutes(){

    val addressDataSource : AddressDataSource by inject()

    route("addresses"){
        // Get all addresses
        get {
            if(!isAdmin()) return@get
            val addresses = addressDataSource.getAllAddresses()
            call.respond(addresses)
        }

        // Get address by id
        get("/{id}"){
            if(!isAdmin()) return@get
            val id = call.parameters["id"] ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest, "Invalid id")
                return@get
            }
            ObjectId.isValid(id)

            val address = addressDataSource.getAddressById(id)
            if(address != null){
                call.respond(address)
            } else {
                call.respond(HttpStatusCode.NotFound, "Address not found")
            }
        }

        // Create new address
        post {
            if(!isAdmin()) return@post
            val request = kotlin.runCatching { call.receiveNullable<AddressRequest>() }.getOrNull() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest, "Invalid request")
                return@post
            }

            val addressRequest = request.toAddress()
            val newAddress = addressDataSource.insertAddress(addressRequest)

            if(newAddress){ call.respond(HttpStatusCode.Created, "Address created") }
            else { call.respond(HttpStatusCode.InternalServerError, "An error occurred") }
        }

        // Update address
        put("/{id}"){
            if(!isAdmin()) return@put
            val id = call.parameters["id"] ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest, "Invalid id")
                return@put
            }
            ObjectId.isValid(id)

            val address = addressDataSource.getAddressById(id)
            if(address == null){
                call.respond(HttpStatusCode.NotFound, "Address not found")
                return@put
            }

            val request = kotlin.runCatching { call.receiveNullable<AddressRequest>() }.getOrNull() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest, "Invalid request")
                return@put
            }

            val addressDomain = request.toAddress().copy(id = ObjectId(id))
            val updatedAddress = addressDataSource.updateAddress(addressDomain)
            if(updatedAddress){
                call.respond(HttpStatusCode.Created, "Address updated")
            } else {
                call.respond(HttpStatusCode.InternalServerError, "An error occurred")
            }
        }

        // Delete address
        delete("/{id}"){
            if(!isAdmin()) return@delete
            val id = call.parameters["id"] ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest, "Invalid id")
                return@delete
            }
            ObjectId.isValid(id)

            val address = addressDataSource.getAddressById(id)
            if(address == null){
                call.respond(HttpStatusCode.NotFound, "Address not found")
                return@delete
            }

            val deletedAddress = addressDataSource.deleteAddress(id)
            if(deletedAddress){ call.respond(HttpStatusCode.OK, "Address deleted") }
            else { call.respond(HttpStatusCode.InternalServerError, "An error occurred") }
        }
    }
}