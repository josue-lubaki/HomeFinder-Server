package ca.josue_lubaki.data.datasource

import ca.josue_lubaki.data.models.Owner
import ca.josue_lubaki.data.response.owner.OwnerResponse

/**
 * @author Josue Lubaki
 * @version 1.0
 * @since 2023-03-05
 */
interface OwnerDataSource {
    suspend fun getAllOwners(): List<OwnerResponse>
    suspend fun getOwnerById(id: String): OwnerResponse?
    suspend fun getOwnerByUsername(username: String): OwnerResponse?
    suspend fun insertOwner(owner: Owner): Boolean
    suspend fun updateOwner(owner: Owner): Boolean
    suspend fun deleteOwner(id: String): Boolean
}