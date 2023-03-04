package ca.josue_lubaki.data.datasource

import ca.josue_lubaki.data.models.User

/**
 * @author Josue Lubaki
 * @version 1.0
 * @since 2023-03-04
 */
interface UserDataSource {
    suspend fun getUserByUsername(username: String): User?
    suspend fun insertUser(user: User): Boolean
}