package ca.josue_lubaki.data.dto

import ca.josue_lubaki.data.models.User

/**
 * @author Josue Lubaki
 * @version 1.0
 * @since 2023-03-04
 */
data class UserDTO (
    val username: String,
    val password: String,
    val email: String,
    val firstName: String,
    val lastName: String,
) {
    fun toModel() = User (
        username = username,
        password = password,
        email = email,
        firstName = firstName,
        lastName = lastName,
        salt = ""
    )
}
