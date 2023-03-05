package ca.josue_lubaki.security.hashing

/**
 * @author Josue Lubaki
 * @version 1.0
 * @since 2023-03-04
 */
data class SaltedHash (
    val hash: String,
    val salt: String,
)
