package ca.josue_lubaki.data.models

import ca.josue_lubaki.data.response.address.AddressResponse
import ca.josue_lubaki.data.response.house.HouseDto
import ca.josue_lubaki.data.response.house.HouseResponse
import ca.josue_lubaki.data.response.owner.OwnerResponse
import ca.josue_lubaki.tools.Utils.Companion.generateIntUUID
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import java.util.UUID

/**
 * @author Josue Lubaki
 * @version 1.0
 * @since 2023-03-05
 */
data class House (
    @BsonId val id: ObjectId = ObjectId(),
    var uuid: Long,
    val description : String,
    val images : List<String>,
    val price : Long,
    val address : String,
    val owner : String,
    val bedrooms : Int,
    val bathrooms : Int,
    val area : Long,
    val type : HouseType,
    val yearBuilt : Int,
    val pool : Boolean,
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
    fun toResponse() = HouseResponse(
        id = id.toString(),
        uuid = uuid,
        description = description,
        images = images,
        price = price,
        address = address,
        owner = owner,
        bedrooms = bedrooms,
        bathrooms = bathrooms,
        area = area,
        type = type.name,
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

    fun toDto() : HouseDto {
        return HouseDto(
            id = id.toString(),
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
            type = type.name,
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
