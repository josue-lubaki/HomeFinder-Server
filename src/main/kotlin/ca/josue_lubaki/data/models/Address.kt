package ca.josue_lubaki.data.models

import ca.josue_lubaki.data.response.address.AddressResponse
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

/**
 * @author Josue Lubaki
 * @version 1.0
 * @since 2023-03-05
 */
data class Address (
    @BsonId val id: ObjectId = ObjectId(),
    val number: String,
    val street: String,
    val city: String,
    val province: String,
    val postalCode: String,
    val country: String
) {
    fun toResponse(): AddressResponse {
        return AddressResponse(
            id = id.toString(),
            number = number,
            street = street,
            city = city,
            province = province,
            postalCode = postalCode,
            country = country
        )
    }

    fun toDomain(): Address {
        return Address(
            id = id,
            number = number,
            street = street,
            city = city,
            province = province,
            postalCode = postalCode,
            country = country
        )
    }
}
