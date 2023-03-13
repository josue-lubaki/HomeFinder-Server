package ca.josue_lubaki.data.response.user

import ca.josue_lubaki.data.models.Role
import kotlinx.serialization.Serializable

/**
 * @author Josue Lubaki
 * @version 1.0
 * @since 2023-03-12
 */

@Serializable
data class UserDto (
    val id: String,
    val username: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val role : String = Role.USER.name
)