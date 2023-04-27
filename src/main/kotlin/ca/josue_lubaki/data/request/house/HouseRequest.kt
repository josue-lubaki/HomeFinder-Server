package ca.josue_lubaki.data.request.house

import ca.josue_lubaki.data.models.House
import ca.josue_lubaki.data.models.HouseType
import ca.josue_lubaki.data.response.address.AddressResponse
import ca.josue_lubaki.data.response.owner.OwnerResponse
import ca.josue_lubaki.tools.Utils.Companion.generateIntUUID
import kotlinx.serialization.Serializable

/**
 * @author Josue Lubaki
 * @version 1.0
 * @since 2023-03-05
 */
@Serializable
data class HouseRequest (
    val description : String,
    val images : List<String>,
    val price : Long,
    val number : String,
    val street : String,
    val city : String,
    val province : String,
    val postalCode : String,
    val country : String,
    val bedrooms : Int,
    val bathrooms : Int,
    val area : Long,
    val type : String,
    val yearBuilt : Int,
    val pool : Boolean,
    val ownerUsername : String,
    val ownerFirstName : String,
    val ownerLastName : String,
    val ownerEmail : String,
    val ownerPhone : String,
    val kitchen: Int,
    val parking: Int,
    val rating : Double,
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
){
    fun toHouse(address : AddressResponse, owner : OwnerResponse) = House(
        description = description,
        uuid = 0,
        images = images,
        price = price,
        address = address.id,
        owner = owner.id,
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
