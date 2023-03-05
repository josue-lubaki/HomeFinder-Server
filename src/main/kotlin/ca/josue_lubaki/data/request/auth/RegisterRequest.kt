package ca.josue_lubaki.data.request.auth

import kotlinx.serialization.Serializable

/**
 * @author Josue Lubaki
 * @version 1.0
 * @since 2023-03-04
 */
@Serializable
data class RegisterRequest (
    val username: String,
    val password: String,
    val email: String,
    val firstName: String,
    val lastName: String
)
