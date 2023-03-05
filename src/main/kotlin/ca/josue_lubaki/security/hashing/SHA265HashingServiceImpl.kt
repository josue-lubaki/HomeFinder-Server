package ca.josue_lubaki.security.hashing

import org.apache.commons.codec.binary.Hex
import org.apache.commons.codec.digest.DigestUtils
import java.security.SecureRandom

/**
 * @author Josue Lubaki
 * @version 1.0
 * @since 2023-03-04
 */
class SHA265HashingServiceImpl : HashingService {

    override fun generateSaltedHash(password: String, saltLength: Int): SaltedHash {
        val salt = SecureRandom.getInstance("SHA1PRNG").generateSeed(saltLength)
        val saltAsHex = Hex.encodeHexString(salt)
        val hash = DigestUtils.sha256Hex("$saltAsHex$password")

        return SaltedHash(
            hash = hash,
            salt = saltAsHex
        )
    }

    override fun verify(password: String, saltedHash: SaltedHash): Boolean {
        return DigestUtils.sha256Hex(saltedHash.salt + password) == saltedHash.hash
    }
}