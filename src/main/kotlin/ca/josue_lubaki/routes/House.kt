package ca.josue_lubaki.routes

import ca.josue_lubaki.data.datasource.AddressDataSource
import ca.josue_lubaki.data.datasource.HouseDataSource
import ca.josue_lubaki.data.datasource.OwnerDataSource
import ca.josue_lubaki.data.models.Address
import ca.josue_lubaki.data.models.HouseType
import ca.josue_lubaki.data.models.Owner
import ca.josue_lubaki.data.models.Role
import ca.josue_lubaki.data.request.house.HouseRequest
import ca.josue_lubaki.data.response.owner.OwnerResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*
import org.bson.types.ObjectId
import org.koin.ktor.ext.inject

/**
 * @author Josue Lubaki
 * @version 1.0
 * @since 2023-03-05
 */

fun Route.housesRoutes() {
    route("houses") {
        val houseDataSource : HouseDataSource by inject()
        val ownerDataSource : OwnerDataSource by inject()
        val addressDataSource : AddressDataSource by inject()

        // Create a house
        post {
            if (!isAdmin()) return@post
            val request = kotlin.runCatching { call.receiveNullable<HouseRequest>() }.getOrNull() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest, "Invalid request")
                return@post
            }

            // verify if the house already exists
            val owner = ownerDataSource.getOwnerByUsername(request.ownerUsername)
            val address = addressDataSource.getAddressByStreetAndNumber(request.street, request.number)

            if (owner != null && address != null){
                val house = houseDataSource.getHouseByOwnerAndAddress(owner.toDomain(), address)
                if (house != null){
                    call.respond(HttpStatusCode.Conflict, "This house already exists")
                    return@post
                }
            }

            val houseRequest = request.toHouse()
            val newHouse = houseDataSource.insertHouse(houseRequest)

            if (newHouse){
                call.respond(HttpStatusCode.Created, "House created")
            } else {
                call.respond(HttpStatusCode.InternalServerError, "An error occurred")
            }
        }

        // get All houses
        get {
            if (!isAdmin()) return@get

            val houses = houseDataSource.getAllHouses()
            if (houses.isNotEmpty()){ call.respond(HttpStatusCode.OK, houses) }
            else { call.respond(HttpStatusCode.NoContent, "No houses found") }
        }

        // get houses by type
        get("/{id}"){
            val id = call.parameters["id"] ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest, "Invalid id")
                return@get
            }
            ObjectId.isValid(id)
            if (!isAdmin()) return@get

            val house = houseDataSource.getHouseById(id)

            if (house != null){ call.respond(HttpStatusCode.OK, house) }
            else { call.respond(HttpStatusCode.NotFound, "House not found") }
        }


        // Update a house
        put("/{id}"){
            val id = call.parameters["id"] ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest, "Invalid id")
                return@put
            }

            if (!isAdmin()) return@put
            ObjectId.isValid(id)

            val house = houseDataSource.getHouseById(id)
            if (house == null){
                call.respond(HttpStatusCode.NotFound, "House not found")
                return@put
            }

            val request = kotlin.runCatching { call.receiveNullable<HouseRequest>() }.getOrNull() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest, "Invalid request")
                return@put
            }

            val owner = ownerDataSource.getOwnerById(id) ?:
            OwnerResponse(
                id = house.owner.id,
                username = request.ownerUsername,
                firstName = request.ownerFirstName,
                lastName = request.ownerLastName,
                email = request.ownerEmail,
                phone = request.ownerPhone
            )

            val updatedOwner = ownerDataSource.updateOwner(owner.toDomain())
            if (!updatedOwner){
                call.respond(HttpStatusCode.InternalServerError, "An error occurred")
                return@put
            }

            val address = addressDataSource.getAddressByStreetAndNumber(request.street, request.number)?.copy(
                city = request.city,
                province = request.province,
                country = request.country,
                postalCode = request.postalCode
            ) ?: Address(
                id = ObjectId(house.address.id),
                street = request.street,
                number = request.number,
                city = request.city,
                province = request.province,
                country = request.country,
                postalCode = request.postalCode
            )

            val updatedAddress = addressDataSource.updateAddress(address.toDomain())
            if (!updatedAddress){
                call.respond(HttpStatusCode.InternalServerError, "An error occurred")
                return@put
            }

            val updatedToHouse = houseDataSource.getHouseById(id)?.copy(
                id = id,
                owner = owner,
                address = address.toResponse(),
                price = request.price,
                description = request.description,
                type = HouseType.valueOf(request.type).name,
                bedrooms = request.bedrooms,
                bathrooms = request.bathrooms,
                area = request.area,
                pool = request.pool,
                yearBuilt = request.yearBuilt,
                images = request.images
            )

            if(updatedToHouse == null){
                call.respond(HttpStatusCode.InternalServerError, "An error occurred")
                return@put
            }

            val houseDomain = updatedToHouse.toDomain()
            val updatedHouse = houseDataSource.updateHouse(houseDomain)
            if(updatedHouse){
                call.respond(HttpStatusCode.OK, "House updated")
            } else {
                call.respond(HttpStatusCode.InternalServerError, "An error occurred")
            }
        }

        // Delete a house
        delete("/{id}"){
            if (!isAdmin()) return@delete
            val id = call.parameters["id"]?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest, "Invalid id")
                return@delete
            }
            ObjectId.isValid(id)
            val deleted = houseDataSource.deleteHouse(id)

            if (deleted) { call.respond(HttpStatusCode.OK, "House deleted") }
            else { call.respond(HttpStatusCode.NotFound, "House not found") }
        }

    }
}