package ca.josue_lubaki.data.datasource.impl

import ca.josue_lubaki.data.datasource.UserDataSource
import ca.josue_lubaki.data.models.ApiResponse
import ca.josue_lubaki.data.models.User
import ca.josue_lubaki.data.response.user.UserResponse
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
 * @since 2023-03-04
 */
class UserDataSourceImpl(
    db : CoroutineDatabase
) : UserDataSource {

    private val users = db.getCollection<User>()

    override suspend fun getAllUsers(page: Int, limit : Int): ApiResponse<UserResponse> {

        val users = pager(users, page, limit)

        return ApiResponse(
            success = true,
            message = HttpStatusCode.OK.description,
            prevPage = Utils.calculatePage(page)[PREV_PAGE_KEY],
            nextPage = Utils.calculatePage(page)[NEXT_PAGE_KEY],
            data = users.toList().map { it.toResponse() },
            lastUpdated = System.currentTimeMillis()
        )
    }

    override suspend fun getUserByUsername(username: String): ApiResponse<UserResponse> {
        return ApiResponse(
            success = true,
            message = HttpStatusCode.OK.description,
            data = listOf(users.findOne(User::username eq username)?.toResponse()),
            lastUpdated = System.currentTimeMillis()
        )
    }

    override suspend fun getUserById(id: String): ApiResponse<UserResponse> {
        return ApiResponse(
            success = true,
            message = HttpStatusCode.OK.description,
            data = listOf(users.findOneById(ObjectId(id))?.toResponse()),
            lastUpdated = System.currentTimeMillis()
        )
    }

    override suspend fun insertUser(user: User): ApiResponse<Boolean> {
        return ApiResponse(
            success = users.insertOne(user).wasAcknowledged(),
            message = HttpStatusCode.OK.description,
            lastUpdated = System.currentTimeMillis()
        )
    }

    override suspend fun updateUser(user: User): ApiResponse<Boolean> {
        return ApiResponse(
            success = users.replaceOne(Filters.eq("_id", user.id), user).wasAcknowledged(),
            message = HttpStatusCode.OK.description,
            lastUpdated = System.currentTimeMillis()
        )
    }

    override suspend fun deleteUser(id: String): ApiResponse<Boolean> {
        return ApiResponse(
            success = users.deleteOne(Filters.eq("_id", ObjectId(id))).wasAcknowledged(),
            message = HttpStatusCode.OK.description,
            lastUpdated = System.currentTimeMillis()
        )
    }
}