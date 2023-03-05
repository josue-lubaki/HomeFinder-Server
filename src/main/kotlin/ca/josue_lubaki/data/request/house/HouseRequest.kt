package ca.josue_lubaki.data.request.house

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
){
    fun toHouse(address : AddressResponse, owner : OwnerResponse) = House(
        description = description,
        images = images,
        price = price,
        address = address.id,
        bedrooms = bedrooms,
        bathrooms = bathrooms,
        area = area,
        type = HouseType.valueOf(type),
        yearBuilt = yearBuilt,
        pool = pool,
        owner = owner.id
    )
}
