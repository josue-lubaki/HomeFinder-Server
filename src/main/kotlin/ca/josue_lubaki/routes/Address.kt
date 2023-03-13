package ca.josue_lubaki.routes

import ca.josue_lubaki.data.datasource.AddressDataSource
import ca.josue_lubaki.data.datasource.HouseDataSource
import ca.josue_lubaki.data.datasource.OwnerDataSource
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
    val houseDataSource : HouseDataSource by inject()
    val ownerDataSource : OwnerDataSource by inject()

    route("addresses"){

        // Get all addresses [GET]
        // http://localhost:8080/api/v1/addresses
        get {
            val addresses = addressDataSource.getAllAddresses()
            call.respond(addresses)
        }

        // Get address by id [GET]
        // http://localhost:8080/api/v1/addresses/{id}
        get("/{id}"){
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

        // Create new address [POST]
        // http://localhost:8080/api/v1/addresses
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

        // Update address [PUT]
        // http://localhost:8080/api/v1/addresses/{id}
        put("/{id}"){
            val id = call.parameters["id"] ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest, "Invalid id")
                return@put
            }
            ObjectId.isValid(id)
            if(!isAdmin()) return@put

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

        // Delete address [DELETE]
        // http://localhost:8080/api/v1/addresses/{id}
        delete("/{id}"){
            val id = call.parameters["id"] ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest, "Invalid id")
                return@delete
            }
            if(!isAdmin()) return@delete
            ObjectId.isValid(id)

            val address = addressDataSource.getAddressById(id)
            if(address == null){
                call.respond(HttpStatusCode.NotFound, "Address not found")
                return@delete
            }

            val houses = houseDataSource.getAllHousesByAddressId(id).data
            val owners = houses.map { owner -> ownerDataSource.getOwnerById(owner!!.owner.id) }

            kotlin.runCatching {
                owners.forEach{ owner -> ownerDataSource.deleteOwner(owner.data.first()!!.id) }
                houses.forEach { house ->
                    houseDataSource.deleteHouse(house?.id!!)
                }
            }.getOrNull() ?: kotlin.run {
                call.respond(HttpStatusCode.InternalServerError, "An error occurred")
                return@delete
            }

            val deletedAddress = addressDataSource.deleteAddress(id)
            if(deletedAddress){ call.respond(HttpStatusCode.OK, "Address deleted") }
            else { call.respond(HttpStatusCode.InternalServerError, "An error occurred") }
        }
    }
}