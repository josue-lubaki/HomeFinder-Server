package ca.josue_lubaki.data.datasource.impl

import ca.josue_lubaki.data.datasource.OwnerDataSource
import ca.josue_lubaki.data.models.Owner
import ca.josue_lubaki.data.response.owner.OwnerResponse
import com.mongodb.client.model.Filters
import org.bson.types.ObjectId
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

/**
 * @author Josue Lubaki
 * @version 1.0
 * @since 2023-03-05
 */
class OwnerDataSourceImpl(
    db : CoroutineDatabase
) : OwnerDataSource {

    private val owners = db.getCollection<Owner>()

    override suspend fun getAllOwners(): List<OwnerResponse> {
        return owners.find().toList().map(Owner::toResponse)
    }

    override suspend fun getOwnerById(id: String): OwnerResponse? {
        return owners.findOneById(ObjectId(id))?.toResponse()
    }

    override suspend fun getOwnerByUsername(username: String): OwnerResponse? {
        return owners.findOne(Owner::username eq username)?.toResponse()
    }

    override suspend fun insertOwner(owner: Owner): Boolean {
        return owners.insertOne(owner).wasAcknowledged()
    }

    override suspend fun updateOwner(owner: Owner): Boolean {
        return owners.replaceOne(Filters.eq("_id", owner.id), owner).wasAcknowledged()
    }

    override suspend fun deleteOwner(id: String): Boolean {
        return owners.deleteOne(Filters.eq("_id", ObjectId(id))).wasAcknowledged()
    }
}