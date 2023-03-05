package ca.josue_lubaki.routes

import ca.josue_lubaki.data.datasource.AddressDataSource
import ca.josue_lubaki.data.datasource.HouseDataSource
import ca.josue_lubaki.data.datasource.OwnerDataSource
import ca.josue_lubaki.data.models.Address
import ca.josue_lubaki.data.models.HouseType
import ca.josue_lubaki.data.models.Owner
import ca.josue_lubaki.data.request.house.HouseRequest
import ca.josue_lubaki.data.response.address.AddressResponse
import ca.josue_lubaki.data.response.house.HouseDto
import ca.josue_lubaki.data.response.house.HouseResponse
import ca.josue_lubaki.data.response.owner.OwnerResponse
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

fun Route.housesRoutes() {
    route("houses") {
        val houseDataSource : HouseDataSource by inject()
        val ownerDataSource : OwnerDataSource by inject()
        val addressDataSource : AddressDataSource by inject()

        // Create a house [POST]
        // http://localhost:8080/api/v1/houses
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
                val house = houseDataSource.getHouseByOwnerAndAddress(owner.id, address.id)
                if (house != null){
                    call.respond(HttpStatusCode.Conflict, "This house already exists")
                    return@post
                }
            }

            // create the owner if it doesn't exist
            if(owner == null){
                val newOwner = Owner(
                    username = request.ownerUsername,
                    firstName = request.ownerFirstName,
                    lastName = request.ownerLastName,
                    email = request.ownerEmail,
                    phone = request.ownerPhone
                )
                ownerDataSource.insertOwner(newOwner)
            }

            // create the address if it doesn't exist
            if(address == null){
                val newAddress = Address(
                    number = request.number,
                    street = request.street,
                    city = request.city,
                    province = request.province,
                    postalCode = request.postalCode,
                    country = request.country
                )
                addressDataSource.insertAddress(newAddress)
            }

            // get the owner and address
            val ownerInserted = ownerDataSource.getOwnerByUsername(request.ownerUsername)!!
            val addressInserted = addressDataSource.getAddressByStreetAndNumber(request.street, request.number)!!
            val houseToInsert = request.toHouse(addressInserted, ownerInserted)
            val newHouse = houseDataSource.insertHouse(houseToInsert)

            if (newHouse){
                call.respond(HttpStatusCode.Created, "House created")
            } else {
                call.respond(HttpStatusCode.InternalServerError, "An error occurred")
            }
        }

        // get All houses [GET]
        // http://localhost:8080/api/v1/houses
        get {
            if (!isAdmin()) return@get

            val houses = houseDataSource.getAllHouses()
            if (houses.isEmpty()){
                call.respond(HttpStatusCode.NoContent, "No houses found")
                return@get
            }

            val houseResponse = mutableListOf<HouseDto>()
            houses.forEach {
                val owner = ownerDataSource.getOwnerById(it.owner)
                val address = addressDataSource.getAddressById(it.address)
                houseResponse.add(houseDto(it, address, owner))
            }

            if(houseResponse.isEmpty()){
                call.respond(HttpStatusCode.NoContent, "No houses found")
                return@get
            }

            call.respond(HttpStatusCode.OK, houseResponse)
        }

        // get houses by type [GET]
        // http://localhost:8080/api/v1/houses/{id}
        get("/{id}"){
            val id = call.parameters["id"] ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest, "Invalid id")
                return@get
            }
            ObjectId.isValid(id)
            if (!isAdmin()) return@get

            val house = houseDataSource.getHouseById(id)

            if (house == null) {
                call.respond(HttpStatusCode.NotFound, "House not found")
                return@get
            }

            val owner = ownerDataSource.getOwnerById(house.owner)
            val address = addressDataSource.getAddressById(house.address)
            val houseResponse = houseDto(house, address, owner)
            call.respond(HttpStatusCode.OK, houseResponse)
        }


        // Update a house [PUT]
        // http://localhost:8080/api/v1/houses/{id}
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

            kotlin.runCatching {
                val owner = ownerDataSource.getOwnerById(house.owner)!!.copy(
                    id = house.owner,
                    username = request.ownerUsername,
                    firstName = request.ownerFirstName,
                    lastName = request.ownerLastName,
                    email = request.ownerEmail,
                    phone = request.ownerPhone
                )

                val address = addressDataSource.getAddressById(house.address)!!.copy(
                    id = house.address,
                    number = request.number,
                    street = request.street,
                    city = request.city,
                    province = request.province,
                    country = request.country,
                    postalCode = request.postalCode
                )

                val addressToUpdate = address.toDomain().copy(id = ObjectId(address.id))
                val updatedAddress = addressDataSource.updateAddress(addressToUpdate)
                val updatedOwner = ownerDataSource.updateOwner(owner.toDomain().copy(id = ObjectId(house.owner)))

                if(!updatedAddress || !updatedOwner){
                    call.respond(HttpStatusCode.InternalServerError, "An error occurred")
                    return@put
                }
            }.getOrNull() ?: kotlin.run {
                call.respond(HttpStatusCode.InternalServerError, "An error occurred")
                return@put
            }

            val updatedToHouse = houseDataSource.getHouseById(id)!!.copy(
                id = id,
                owner = house.owner,
                address = house.address,
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

            val houseToUpdate = updatedToHouse.toDomain().copy(id = ObjectId(id))
            val updatedHouse = houseDataSource.updateHouse(houseToUpdate)
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

            val houseToDelete = houseDataSource.getHouseById(id) ?: kotlin.run {
                call.respond(HttpStatusCode.NotFound, "House not found")
                return@delete
            }

            val deleted = kotlin.runCatching {
                ownerDataSource.deleteOwner(houseToDelete.owner) // delete owner
                addressDataSource.deleteAddress(houseToDelete.address) // delete address
                houseDataSource.deleteHouse(id) // delete house
            }.getOrNull() ?: kotlin.run {
                call.respond(HttpStatusCode.InternalServerError, "An error occurred")
                return@delete
            }

            if (deleted) { call.respond(HttpStatusCode.OK, "House deleted") }
            else { call.respond(HttpStatusCode.NotFound, "House not found") }
        }
    }
}

private fun houseDto(
    house: HouseResponse,
    address: AddressResponse?,
    owner: OwnerResponse?
) = HouseDto(
    id = house.id,
    description = house.description,
    images = house.images,
    price = house.price,
    address = AddressResponse(
        id = address!!.id,
        street = address.street,
        number = address.number,
        city = address.city,
        province = address.province,
        country = address.country,
        postalCode = address.postalCode
    ),
    bedrooms = house.bedrooms,
    bathrooms = house.bathrooms,
    area = house.area,
    type = house.type,
    yearBuilt = house.yearBuilt,
    pool = house.pool,
    owner = OwnerResponse(
        id = owner!!.id,
        username = owner.username,
        firstName = owner.firstName,
        lastName = owner.lastName,
        email = owner.email,
        phone = owner.phone
    ),
)