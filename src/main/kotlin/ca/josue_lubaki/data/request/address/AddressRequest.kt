package ca.josue_lubaki.data.request.address

import ca.josue_lubaki.data.models.Address
import kotlinx.serialization.Serializable

/**
 * @author Josue Lubaki
 * @version 1.0
 * @since 2023-03-05
 */
@Serializable
data class AddressRequest(
    val number: String,
    val street: String,
    val city: String,
    val province: String,
    val postalCode: String,
    val country: String
) {
    fun toAddress(): Address {
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
