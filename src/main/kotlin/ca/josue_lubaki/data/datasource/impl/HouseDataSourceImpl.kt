package ca.josue_lubaki.data.datasource.impl

import ca.josue_lubaki.data.datasource.HouseDataSource
import ca.josue_lubaki.data.models.Address
import ca.josue_lubaki.data.models.House
import ca.josue_lubaki.data.models.Owner
import ca.josue_lubaki.data.response.house.HouseResponse
import com.mongodb.client.model.Filters
import org.bson.types.ObjectId
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.replaceUpsert

/**
 * @author Josue Lubaki
 * @version 1.0
 * @since 2023-03-05
 */
class HouseDataSourceImpl(
    db : CoroutineDatabase
) : HouseDataSource {

    private val houses = db.getCollection<House>()

    override suspend fun getAllHouses(): List<HouseResponse> {
        return houses.find().toList().map(House::toResponse)
    }

    override suspend fun getHouseById(id: String): HouseResponse? {
        return houses.findOneById(ObjectId(id))?.toResponse()
    }

    override suspend fun getHouseByOwnerAndAddress(owner: Owner, address: Address): HouseResponse? {
        val house = houses.findOneById((address.id))
        return if (house?.owner == owner) house.toResponse() else null
    }

    override suspend fun insertHouse(house: House): Boolean {
        return houses.insertOne(house).wasAcknowledged()
    }

    override suspend fun updateHouse(house: House): Boolean {
        return houses.replaceOne(Filters.eq("_id", house.id), house).wasAcknowledged()
    }

    override suspend fun deleteHouse(id: String): Boolean {
        return houses.deleteOne(Filters.eq("_id", ObjectId(id))).wasAcknowledged()
    }
}