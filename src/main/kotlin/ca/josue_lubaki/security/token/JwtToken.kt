package ca.josue_lubaki.security.token

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.*

/**
 * @author Josue Lubaki
 * @version 1.0
 * @since 2023-03-04
 */
class JwtToken : TokenService {

    override fun generateToken(config: TokenConfig, vararg claims: TokenClaim): String {
        val token = JWT.create()
            .withIssuer(config.issuer)
            .withAudience(config.audience)
            .withExpiresAt(Date(System.currentTimeMillis() + config.expiresIn))

        claims.forEach { claim ->
            token.withClaim(claim.name, claim.value)
        }

        return token.sign(Algorithm.HMAC256(config.secret))

    }
}