package ca.josue_lubaki.data.response.owner

import ca.josue_lubaki.data.models.Owner
import kotlinx.serialization.Serializable

/**
 * @author Josue Lubaki
 * @version 1.0
 * @since 2023-03-05
 */
@Serializable
data class OwnerResponse(
    val id: String,
    val username: String,
    val firstName : String,
    val lastName : String,
    val email: String,
    val phone: String,
) {
    fun toDomain(): Owner {
        return Owner(
            username = username,
            firstName = firstName,
            lastName = lastName,
            email = email,
            phone = phone
        )
    }
}
