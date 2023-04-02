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
    val bedrooms : Int,
    val bathrooms : Int,
    val area : Long,
    val type : HouseType,
    val yearBuilt : Int,
    val pool : Boolean,
    val owner : String,
) {
    fun toResponse() = HouseResponse(
        id = id.toString(),
        uuid = uuid,
        description = description,
        images = images,
        price = price,
        address = address,
        bedrooms = bedrooms,
        bathrooms = bathrooms,
        area = area,
        type = type.name,
        yearBuilt = yearBuilt,
        pool = pool,
        owner = owner
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
            bedrooms = bedrooms,
            bathrooms = bathrooms,
            area = area,
            type = type.name,
            yearBuilt = yearBuilt,
            pool = pool,
            owner = OwnerResponse(
                id = owner,
                uuid = 0,
                username = "",
                firstName = "",
                lastName = "",
                email = "",
                phone = "",
            )
        )
    }
}
