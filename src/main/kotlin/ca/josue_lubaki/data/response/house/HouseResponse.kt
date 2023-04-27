package ca.josue_lubaki.data.response.house

import ca.josue_lubaki.data.models.House
import ca.josue_lubaki.data.models.HouseType
import ca.josue_lubaki.data.response.address.AddressResponse
import ca.josue_lubaki.data.response.owner.OwnerResponse
import kotlinx.serialization.Serializable

/**
 * @author Josue Lubaki
 * @version 1.0
 * @since 2023-03-05
 */
@Serializable
data class HouseResponse (
    val id: String,
    val uuid : Long,
    val description: String,
    val images: List<String>,
    val price: Long,
    val address: String,
    val bedrooms: Int,
    val bathrooms: Int,
    val area: Long,
    val type: String,
    val yearBuilt: Int,
    val pool: Boolean,
    val owner: String,
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
    fun toDomain(): House {
        return House(
            uuid = uuid,
            description = description,
            images = images,
            price = price,
            address = address,
            owner = owner,
            bedrooms = bedrooms,
            bathrooms = bathrooms,
            area = area,
            type = HouseType.valueOf(type),
            yearBuilt = yearBuilt,
            pool = pool,
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

    fun toDto() : HouseDto {
        return HouseDto(
            id = id,
            uuid = uuid,
            description = description,
            images = images,
            price = price,
            address = AddressResponse(
                id = address,
                number = "",
                street = "",
                city = "",
                province = "",
                postalCode = "",
                country = ""
            ),
            owner = OwnerResponse(
                id = owner,
                uuid = 0,
                username = "",
                firstName = "",
                lastName = "",
                email = "",
                phone = "",
            ),
            bedrooms = bedrooms,
            bathrooms = bathrooms,
            area = area,
            type = type,
            yearBuilt = yearBuilt,
            pool = pool,
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
