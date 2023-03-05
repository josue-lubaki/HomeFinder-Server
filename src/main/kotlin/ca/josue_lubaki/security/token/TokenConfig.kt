package ca.josue_lubaki.security.token

/**
 * @author Josue Lubaki
 * @version 1.0
 * @since 2023-03-04
 */

data class TokenConfig (
    val issuer: String,
    val audience: String,
    val expiresIn: Long,
    val secret: String,
)