package ca.josue_lubaki.data.response.house

import ca.josue_lubaki.data.response.address.AddressResponse
import ca.josue_lubaki.data.response.owner.OwnerResponse
import kotlinx.serialization.Serializable
import java.util.UUID

/**
 * @author Josue Lubaki
 * @version 1.0
 * @since 2023-03-05
 */
@Serializable
data class HouseDto (
    val id: String,
    val uuid: Long,
    val description: String,
    val images: List<String>,
    val price: Long,
    val address: AddressResponse,
    val bedrooms: Int,
    val bathrooms: Int,
    val area: Long,
    val type: String,
    val yearBuilt: Int,
    val pool: Boolean,
    val owner: OwnerResponse
) {
    fun toResponse(): HouseResponse {
        return HouseResponse(
            id = id,
            uuid = uuid,
            description = description,
            images = images,
            price = price,
            address = address.id,
            bedrooms = bedrooms,
            bathrooms = bathrooms,
            area = area,
            type = type,
            yearBuilt = yearBuilt,
            pool = pool,
            owner = owner.id
        )
    }
}
