package ca.josue_lubaki.routes

import ca.josue_lubaki.data.datasource.AddressDataSource
import ca.josue_lubaki.data.datasource.HouseDataSource
import ca.josue_lubaki.data.datasource.OwnerDataSource
import ca.josue_lubaki.data.models.Address
import ca.josue_lubaki.data.models.Owner
import ca.josue_lubaki.data.request.house.HouseRequest
import ca.josue_lubaki.data.response.address.AddressResponse
import ca.josue_lubaki.data.models.ApiResponse
import ca.josue_lubaki.data.response.house.HouseDto
import ca.josue_lubaki.data.response.house.HouseResponse
import ca.josue_lubaki.data.response.owner.OwnerResponse
import ca.josue_lubaki.tools.Utils.Companion.initPageAndLimit
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.bson.types.ObjectId
import org.koin.ktor.ext.inject
import kotlin.properties.Delegates

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
            val ownerApiResponse = ownerDataSource.getOwnerByUsername(request.ownerUsername)
            val owner = ownerApiResponse.data.first()
            val address = addressDataSource.getAddressByStreetAndNumber(request.street, request.number)

            if (owner != null && address != null){
                val apiResponse = houseDataSource.getHouseByOwnerAndAddress(owner.id, address.id)
                if (apiResponse.data.isNotEmpty()){
                    call.respond(HttpStatusCode.Conflict, "This house already exists")
                    return@post
                }
            }

            // create the owner if it doesn't exist
            if(owner == null){
                val newOwner = Owner(
                    uuid = 0,
                    username = request.ownerUsername,
                    firstName = request.ownerFirstName,
                    lastName = request.ownerLastName,
                    email = request.ownerEmail,
                    phone = request.ownerPhone
                )
                val nextUuid = ownerDataSource.getOwnersSize() + 1
                ownerDataSource.insertOwner(newOwner.apply { uuid = nextUuid })
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
            val ownerInserted = ownerDataSource.getOwnerByUsername(request.ownerUsername).data.first()!!
            val addressInserted = addressDataSource.getAddressByStreetAndNumber(request.street, request.number)!!
            val houseToInsert = request.toHouse(addressInserted, ownerInserted)
            val nextUuid = houseDataSource.getHouseSize() + 1
            val newHouse = houseDataSource.insertHouse(houseToInsert.apply { uuid = nextUuid })

            call.respond(status = HttpStatusCode.OK, message = newHouse)
        }

        // get All houses [GET]
        // http://localhost:8080/api/v1/houses
        get {

            val (page, limit) = initPageAndLimit()

            val apiResponse = houseDataSource.getAllHouses(page, limit)

            val houseResponse = mutableListOf<HouseDto>()
            apiResponse.data.forEach {
                if (it == null) return@forEach
                val owner = ownerDataSource.getOwnerById(it.owner.id).data.first()
                val address = addressDataSource.getAddressById(it.address.id)
                houseResponse.add(houseDto(it.toResponse(), address, owner))
            }

            val response = ApiResponse(
                success = true,
                message = HttpStatusCode.OK.description,
                prevPage = apiResponse.prevPage,
                nextPage = apiResponse.nextPage,
                data = houseResponse.toList(),
                lastUpdated = apiResponse.lastUpdated
            )

            call.respond(HttpStatusCode.OK, response)
        }

        // get houses by type [GET]
        // http://localhost:8080/api/v1/houses/{id}
        get("/{id}"){
            val id = call.parameters["id"] ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest, "Invalid id")
                return@get
            }
            ObjectId.isValid(id)

            val apiResponse = houseDataSource.getHouseById(id)

            val first = apiResponse.data[0] ?: kotlin.run {
                call.respond(HttpStatusCode.NotFound, "House not found")
                return@get
            }

            val owner = ownerDataSource.getOwnerById(first.owner.id).data.first()
            val address = addressDataSource.getAddressById(first.address.id)
            val houseResponse = houseDto(first.toResponse(), address, owner)

            val response  = ApiResponse(
                success = true,
                message = HttpStatusCode.OK.description,
                data = listOf(houseResponse),
                lastUpdated = apiResponse.lastUpdated
            )

            call.respond(HttpStatusCode.OK, response)
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

            val apiResponse = houseDataSource.getHouseById(id)
            val first = apiResponse.data[0]?: kotlin.run {
                call.respond(HttpStatusCode.NotFound, "House not found")
                return@put
            }

            val request = kotlin.runCatching { call.receiveNullable<HouseRequest>() }.getOrNull() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest, "Invalid request")
                return@put
            }

            kotlin.runCatching {
                val owner = ownerDataSource.getOwnerById(first.owner.id).data.first()!!.copy(
                    id = first.owner.id,
                    username = request.ownerUsername,
                    firstName = request.ownerFirstName,
                    lastName = request.ownerLastName,
                    email = request.ownerEmail,
                    phone = request.ownerPhone
                )

                val address = addressDataSource.getAddressById(first.address.id)!!.copy(
                    id = first.address.id,
                    number = request.number,
                    street = request.street,
                    city = request.city,
                    province = request.province,
                    country = request.country,
                    postalCode = request.postalCode
                )

                val addressToUpdate = address.toDomain().copy(id = ObjectId(address.id))
                val updatedAddress = addressDataSource.updateAddress(addressToUpdate)
                val updatedOwnerApiResponse = ownerDataSource.updateOwner(owner.toDomain().copy(id = ObjectId(first.owner.id)))

                if(!updatedAddress || !updatedOwnerApiResponse.success){
                    call.respond(HttpStatusCode.InternalServerError, "An error occurred")
                    return@put
                }
            }.getOrNull() ?: kotlin.run {
                call.respond(HttpStatusCode.InternalServerError, "An error occurred")
                return@put
            }

            val apiResponseUpdated = houseDataSource.getHouseById(id).copy(
                data = listOf(first.copy(
                    id = first.id,
                    price = request.price,
                    bathrooms = request.bathrooms,
                    bedrooms = request.bedrooms,
                    description = request.description,
                    area = request.area,
                    type = request.type,
                    yearBuilt = request.yearBuilt,
                    pool = request.pool,
                    images = request.images,
                    owner = first.owner.copy(
                        id = first.owner.id,
                        username = request.ownerUsername,
                        firstName = request.ownerFirstName,
                        lastName = request.ownerLastName,
                        email = request.ownerEmail,
                        phone = request.ownerPhone
                    ),
                    address = first.address.copy(
                        id = first.address.id,
                        street = request.street,
                        number = request.number,
                        city = request.city,
                        province = request.province,
                        country = request.country,
                        postalCode = request.postalCode
                    )
                ))
            )

            val houseToUpdate = apiResponseUpdated.data[0]?.toResponse()!!
            val updatedHouse = houseDataSource.updateHouse(houseToUpdate.toDomain().copy(id = ObjectId(first.id)))
            if(updatedHouse.success){
                call.respond(HttpStatusCode.OK, updatedHouse)
            } else {
                call.respond(HttpStatusCode.InternalServerError, "An error occurred")
            }
        }

        // Delete a house
        delete("/{id}"){
            if (!isAdmin()) return@delete

            val responseBadRequest = ApiResponse<HouseDto>(
                success = false,
                message = "Invalid id"
            )

            val id = call.parameters["id"] ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest, responseBadRequest)
                return@delete
            }

            if(!ObjectId.isValid(id)){
                call.respond(HttpStatusCode.BadRequest, responseBadRequest)
                return@delete
            }

            val houseToDelete = houseDataSource.getHouseById(id).data[0] ?: kotlin.run {
                call.respond(HttpStatusCode.NoContent)
                return@delete
            }

            val apiResponse = kotlin.runCatching {
                ownerDataSource.deleteOwner(houseToDelete.owner.id) // delete owner
                addressDataSource.deleteAddress(houseToDelete.address.id) // delete address
                houseDataSource.deleteHouse(id) // delete house
            }.getOrNull() ?: kotlin.run {
                call.respond(HttpStatusCode.InternalServerError, "An error occurred")
                return@delete
            }

            if (apiResponse.success) { call.respond(HttpStatusCode.NoContent, apiResponse) }
            else {
                val response = ApiResponse<HouseDto>(
                    success = false,
                    message = "An error occurred while deleting the house"
                )
                call.respond(HttpStatusCode.NotFound, response)
            }
        }
    }
}

private fun houseDto(
    house: HouseResponse,
    address: AddressResponse?,
    owner: OwnerResponse?
) = HouseDto(
    id = house.id,
    uuid = house.uuid,
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
        uuid = owner.uuid,
        username = owner.username,
        firstName = owner.firstName,
        lastName = owner.lastName,
        email = owner.email,
        phone = owner.phone
    ),
)