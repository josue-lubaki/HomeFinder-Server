package ca.josue_lubaki.data.request

import kotlinx.serialization.Serializable

/**
 * @author Josue Lubaki
 * @version 1.0
 * @since 2023-03-04
 */

@Serializable
data class AuthRequest (
    val username: String,
    val password: String,
)
