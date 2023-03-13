package ca.josue_lubaki.data.datasource

import ca.josue_lubaki.data.models.ApiResponse
import ca.josue_lubaki.data.models.Owner
import ca.josue_lubaki.data.response.owner.OwnerResponse

/**
 * @author Josue Lubaki
 * @version 1.0
 * @since 2023-03-05
 */
interface OwnerDataSource {
    suspend fun getAllOwners(page : Int = 1, limit : Int = 5): ApiResponse<OwnerResponse>
    suspend fun getOwnerById(id: String): ApiResponse<OwnerResponse>
    suspend fun getOwnerByUsername(username: String): ApiResponse<OwnerResponse>
    suspend fun insertOwner(owner: Owner): ApiResponse<Boolean>
    suspend fun updateOwner(owner: Owner): ApiResponse<Boolean>
    suspend fun deleteOwner(id: String): ApiResponse<Boolean>
}