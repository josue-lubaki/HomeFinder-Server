package ca.josue_lubaki.data.request.owner

import ca.josue_lubaki.data.models.Owner
import kotlinx.serialization.Serializable

/**
 * @author Josue Lubaki
 * @version 1.0
 * @since 2023-03-05
 */
@Serializable
data class OwnerRequest (
    val username : String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phone: String,
) {
    fun toOwner() = Owner(
        uuid = 0,
        username = username,
        firstName = firstName,
        lastName = lastName,
        email = email,
        phone = phone,
    )
}
