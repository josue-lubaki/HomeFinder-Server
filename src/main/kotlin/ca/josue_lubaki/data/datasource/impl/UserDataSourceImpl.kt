package ca.josue_lubaki.data.datasource.impl

import ca.josue_lubaki.data.datasource.UserDataSource
import ca.josue_lubaki.data.models.User
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

    override suspend fun getUserByUsername(username: String): User? {
        return users.findOne(User::username eq username)
    }

    override suspend fun insertUser(user: User): Boolean {
        return users.insertOne(user).wasAcknowledged()
    }
}