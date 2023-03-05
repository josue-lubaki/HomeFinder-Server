package ca.josue_lubaki.data.response.auth

import kotlinx.serialization.Serializable

/**
 * @author Josue Lubaki
 * @version 1.0
 * @since 2023-03-04
 */
@Serializable
data class AuthResponse (
    val token: String,
)
