package ca.josue_lubaki.data.response.house

import ca.josue_lubaki.data.models.House
import ca.josue_lubaki.data.models.HouseType
import ca.josue_lubaki.data.response.owner.OwnerResponse
import ca.josue_lubaki.data.response.address.AddressResponse
import kotlinx.serialization.Serializable

/**
 * @author Josue Lubaki
 * @version 1.0
 * @since 2023-03-05
 */
@Serializable
data class HouseResponse (
    val id: String,
    val description: String,
    val images: List<String>,
    val price: Long,
    val address: AddressResponse,
    val city: String,
    val bedrooms: Int,
    val bathrooms: Int,
    val area: Long,
    val type: String,
    val yearBuilt: Int,
    val pool: Boolean,
    val owner: OwnerResponse
) {
    fun toDomain(): House {
        return House(
            description = description,
            images = images,
            price = price,
            address = address.toDomain(),
            city = city,
            bedrooms = bedrooms,
            bathrooms = bathrooms,
            area = area,
            type = HouseType.valueOf(type),
            yearBuilt = yearBuilt,
            pool = pool,
            owner = owner.toDomain()
        )
    }
}
