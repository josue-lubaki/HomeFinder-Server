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
    val owner: OwnerResponse,
    val kitchen: Int,
    val parking: Int,
    val rating : Int,
    val isFeature: Boolean,
    val isSold: Boolean,
    val toRent: Boolean,
    val toSell: Boolean,
    val hasGarage: Boolean,
    val hasTerrace: Boolean,
    val isHeating: Boolean,
    val isFurnished : Boolean,
    val isPetFriendly : Boolean,
    val hasAirConditioning : Boolean,
    val hasInternetAccess : Boolean,
    val hasWasher : Boolean,
    val hasElectricity : Boolean,
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
            owner = owner.id,
            kitchen = kitchen,
            parking = parking,
            rating = rating,
            isFeature = isFeature,
            isSold = isSold,
            toRent = toRent,
            toSell = toSell,
            hasGarage = hasGarage,
            hasTerrace = hasTerrace,
            isHeating = isHeating,
            isFurnished = isFurnished,
            isPetFriendly = isPetFriendly,
            hasAirConditioning = hasAirConditioning,
            hasInternetAccess = hasInternetAccess,
            hasWasher = hasWasher,
            hasElectricity = hasElectricity,
        )
    }
}
