package ca.josue_lubaki.security.hashing

/**
 * @author Josue Lubaki
 * @version 1.0
 * @since 2023-03-04
 */
interface HashingService {
    fun generateSaltedHash(password: String, saltLength : Int = 32): SaltedHash
    fun verify(password: String, saltedHash: SaltedHash): Boolean
}