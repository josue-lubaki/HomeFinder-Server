package ca.josue_lubaki.data.models

import ca.josue_lubaki.data.response.address.AddressResponse
import ca.josue_lubaki.data.response.house.HouseResponse
import ca.josue_lubaki.data.response.owner.OwnerResponse
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

/**
 * @author Josue Lubaki
 * @version 1.0
 * @since 2023-03-05
 */
data class House (
    @BsonId val id: ObjectId = ObjectId(),
    val description : String,
    val images : List<String>,
    val price : Long,
    val address : Address,
    val city : String,
    val bedrooms : Int,
    val bathrooms : Int,
    val area : Long,
    val type : HouseType,
    val yearBuilt : Int,
    val pool : Boolean,
    val owner : Owner,
) {
    fun toResponse() = HouseResponse(
        id = id.toString(),
        description = description,
        images = images,
        price = price,
        address = AddressResponse(
            id = address.id.toString(),
            number = address.number,
            street = address.street,
            city = address.city,
            province = address.province,
            postalCode = address.postalCode,
            country = address.country
        ),
        city = city,
        bedrooms = bedrooms,
        bathrooms = bathrooms,
        area = area,
        type = type.name,
        yearBuilt = yearBuilt,
        pool = pool,
        owner = OwnerResponse(
            id = owner.id.toString(),
            username = owner.username,
            firstName = owner.firstName,
            lastName = owner.lastName,
            email = owner.email,
            phone = owner.phone,
        )
    )
}
