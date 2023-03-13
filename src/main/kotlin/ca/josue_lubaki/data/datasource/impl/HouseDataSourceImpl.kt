package ca.josue_lubaki.data.datasource.impl

import ca.josue_lubaki.data.datasource.HouseDataSource
import ca.josue_lubaki.data.models.House
import ca.josue_lubaki.data.models.ApiResponse
import ca.josue_lubaki.data.response.house.HouseDto
import ca.josue_lubaki.tools.NEXT_PAGE_KEY
import ca.josue_lubaki.tools.PREV_PAGE_KEY
import ca.josue_lubaki.tools.Utils
import com.mongodb.client.model.Filters
import io.ktor.http.*
import org.bson.types.ObjectId
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

/**
 * @author Josue Lubaki
 * @version 1.0
 * @since 2023-03-05
 */

class HouseDataSourceImpl(
    db : CoroutineDatabase
) : HouseDataSource {

    private val houses = db.getCollection<House>()

    override suspend fun getAllHouses(page : Int, limit : Int): ApiResponse<HouseDto> {

        // split to pages
        val houses = houses.find().skip((page - 1) * limit).limit(limit)

        return ApiResponse(
            success = true,
            message = HttpStatusCode.OK.description,
            prevPage = Utils.calculatePage(page)[PREV_PAGE_KEY],
            nextPage = Utils.calculatePage(page)[NEXT_PAGE_KEY],
            data = houses.toList().map { it.toDto() },
            lastUpdated = System.currentTimeMillis()
        )
    }

    override suspend fun getHouseById(id: String): ApiResponse<HouseDto> {
        return ApiResponse(
            success = true,
            message = HttpStatusCode.OK.description,
            data = listOf(houses.findOneById(ObjectId(id))?.toDto()),
            lastUpdated = System.currentTimeMillis()
        )
    }

    override suspend fun getHouseByOwnerAndAddress(ownerId: String, addressId: String): ApiResponse<HouseDto> {
        return ApiResponse(
            success = true,
            message = HttpStatusCode.OK.description,
            data = listOf(houses.findOne(House::owner eq ownerId, House::address eq addressId)?.toDto()),
            lastUpdated = System.currentTimeMillis()
        )
    }

    override suspend fun insertHouse(house: House): ApiResponse<HouseDto> {
        return ApiResponse(
            success = houses.insertOne(house).wasAcknowledged(),
            message = HttpStatusCode.OK.description,
            lastUpdated = System.currentTimeMillis()
        )
    }

    override suspend fun updateHouse(house: House): ApiResponse<HouseDto> {
        return ApiResponse(
            success = houses.replaceOne(Filters.eq("_id", house.id), house).wasAcknowledged(),
            message = HttpStatusCode.OK.description,
            lastUpdated = System.currentTimeMillis()
        )
    }

    override suspend fun deleteHouse(id: String): ApiResponse<HouseDto> {
        return ApiResponse(
            success = houses.deleteOne(Filters.eq("_id", ObjectId(id))).wasAcknowledged(),
            message = "House deleted successfully",
            lastUpdated = System.currentTimeMillis()
        )
    }

    override suspend fun getHouseByAddressId(id: String): ApiResponse<HouseDto> {
        return ApiResponse(
            success = true,
            message = HttpStatusCode.OK.description,
            data = listOf(houses.findOne(House::address eq id)?.toDto()),
            lastUpdated = System.currentTimeMillis()
        )
    }

    override suspend fun getAllHousesByAddressId(id: String): ApiResponse<HouseDto> {
        return ApiResponse(
            success = true,
            message = HttpStatusCode.OK.description,
            data = houses.find(House::address eq id).toList().map { it.toDto() },
            lastUpdated = System.currentTimeMillis()
        )
    }
}