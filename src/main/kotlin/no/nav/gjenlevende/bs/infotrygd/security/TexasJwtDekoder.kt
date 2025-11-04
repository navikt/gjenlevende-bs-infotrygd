package no.nav.gjenlevende.bs.infotrygd.security

import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtException
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class TexasJwtDekoder(
    private val texasClient: TexasClient,
) : JwtDecoder {
    override fun decode(token: String): Jwt {
        try {
            val introspection = texasClient.introspectToken(token)

            if (!introspection.active) {
                throw JwtException(introspection.error ?: "Token er ikke aktiv")
            }

            val claims = mutableMapOf<String, Any>()
            introspection.sub?.let { claims["sub"] = it }
            introspection.aud?.let { claims["aud"] = it }
            introspection.exp?.let { claims["exp"] = it }
            introspection.iat?.let { claims["iat"] = it }


            val parts = token.split(".")
            if (parts.size != 3) {
                throw JwtException("Ugyldig JWT format")
            }

            // TODO: Usikker på om det trenger med manuell mapping her, tester.
            val headers = mapOf(
                "alg" to "RS256",
                "typ" to "JWT",
            )

            // TODO: Setter Instant.now(), WIP. Jobber med å utbedre dekoding.
            return Jwt(
                token,
                introspection.iat?.let { Instant.ofEpochSecond(it) } ?: Instant.now(),
                introspection.exp?.let { Instant.ofEpochSecond(it) } ?: Instant.now().plusSeconds(3600),
                headers,
                claims,
            )
        } catch (e: Exception) {
            throw JwtException("Token validering feilet med melding: ${e.message}", e)
        }
    }
}
