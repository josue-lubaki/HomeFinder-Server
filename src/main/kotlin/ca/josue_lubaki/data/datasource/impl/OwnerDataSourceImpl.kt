package ca.josue_lubaki.data.datasource.impl

import ca.josue_lubaki.data.datasource.OwnerDataSource
import ca.josue_lubaki.data.models.ApiResponse
import ca.josue_lubaki.data.models.Owner
import ca.josue_lubaki.data.response.owner.OwnerResponse
import ca.josue_lubaki.tools.NEXT_PAGE_KEY
import ca.josue_lubaki.tools.PREV_PAGE_KEY
import ca.josue_lubaki.tools.Utils
import ca.josue_lubaki.tools.Utils.Companion.pager
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
class OwnerDataSourceImpl(
    db : CoroutineDatabase
) : OwnerDataSource {

    private val owners = db.getCollection<Owner>()

    override suspend fun getAllOwners(page : Int, limit : Int): ApiResponse<OwnerResponse> {

        val owners = pager(owners, page, limit)
        return ApiResponse(
            success = true,
            message = HttpStatusCode.OK.description,
            prevPage = Utils.calculatePage(page)[PREV_PAGE_KEY],
            nextPage = Utils.calculatePage(page)[NEXT_PAGE_KEY],
            data = owners.toList().map { it.toResponse() },
            lastUpdated = System.currentTimeMillis()
        )
    }

    override suspend fun getOwnerById(id: String): ApiResponse<OwnerResponse> {
        return ApiResponse(
            success = true,
            message = HttpStatusCode.OK.description,
            data = listOf(owners.findOneById(ObjectId(id))?.toResponse()),
            lastUpdated = System.currentTimeMillis()
        )
    }

    override suspend fun getOwnerByUsername(username: String): ApiResponse<OwnerResponse> {
        return ApiResponse(
            success = true,
            message = HttpStatusCode.OK.description,
            data = listOf(owners.findOne(Owner::username eq username)?.toResponse()),
            lastUpdated = System.currentTimeMillis()
        )
    }

    override suspend fun insertOwner(owner: Owner): ApiResponse<Boolean> {
        return ApiResponse(
            success = owners.insertOne(owner).wasAcknowledged(),
            message = HttpStatusCode.OK.description,
            lastUpdated = System.currentTimeMillis()
        )
    }

    override suspend fun updateOwner(owner: Owner): ApiResponse<Boolean> {
        return ApiResponse(
            success = owners.replaceOne(Filters.eq("_id", owner.id), owner).wasAcknowledged(),
            message = HttpStatusCode.OK.description,
            lastUpdated = System.currentTimeMillis()
        )
    }

    override suspend fun deleteOwner(id: String): ApiResponse<Boolean> {
        return ApiResponse(
            success = owners.deleteOne(Filters.eq("_id", ObjectId(id))).wasAcknowledged(),
            message = HttpStatusCode.OK.description,
            lastUpdated = System.currentTimeMillis()
        )
    }

    override suspend fun getOwnersSize(): Long {
        return owners.countDocuments()
    }
}