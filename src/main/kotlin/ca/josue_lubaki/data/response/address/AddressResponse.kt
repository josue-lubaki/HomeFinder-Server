package ca.josue_lubaki.data.response.address

import ca.josue_lubaki.data.models.Address
import kotlinx.serialization.Serializable

/**
 * @author Josue Lubaki
 * @version 1.0
 * @since 2023-03-05
 */
@Serializable
data class AddressResponse (
    val id: String?,
    val number: String?,
    val street: String?,
    val city: String?,
    val province: String?,
    val postalCode: String?,
    val country: String?
) {
    fun toDomain(): Address {
        return Address(
            number = number,
            street = street,
            city = city,
            province = province,
            postalCode = postalCode,
            country = country
        )
    }
}