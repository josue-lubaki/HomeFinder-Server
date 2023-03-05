package ca.josue_lubaki.security.token

/**
 * @author Josue Lubaki
 * @version 1.0
 * @since 2023-03-04
 */
interface TokenService {
    fun generateToken(
        config : TokenConfig,
        vararg claims: TokenClaim
    ) : String
}