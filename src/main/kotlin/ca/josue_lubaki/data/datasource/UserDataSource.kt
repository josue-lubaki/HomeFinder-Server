package ca.josue_lubaki.data.datasource

import ca.josue_lubaki.data.models.ApiResponse
import ca.josue_lubaki.data.models.User
import ca.josue_lubaki.data.response.user.UserResponse

/**
 * @author Josue Lubaki
 * @version 1.0
 * @since 2023-03-04
 */
interface UserDataSource {
    suspend fun getAllUsers(page : Int = 1, limit : Int = 5): ApiResponse<UserResponse>
    suspend fun getUserByUsername(username: String): ApiResponse<UserResponse>
    suspend fun getUserById(id: String): ApiResponse<UserResponse>
    suspend fun insertUser(user: User): ApiResponse<Boolean>
    suspend fun updateUser(user: User): ApiResponse<Boolean>
    suspend fun deleteUser(id: String): ApiResponse<Boolean>
}