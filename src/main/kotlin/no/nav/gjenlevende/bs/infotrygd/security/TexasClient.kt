package no.nav.gjenlevende.bs.infotrygd.security

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono

@Component
class TexasClient(
    @Value("\${nais.token.introspection.endpoint}")
    private val introspectionEndpoint: String,
) {
    private val webClient = WebClient.builder().build()

    fun introspectToken(token: String): TokenIntrospectionResponse {
        return webClient.post()
            .uri(introspectionEndpoint)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(
                mapOf(
                    "identity_provider" to "azuread",
                    "token" to token,
                ),
            )
            .retrieve()
            .bodyToMono<TokenIntrospectionResponse>()
            .block() ?: throw RuntimeException("Klarte ikke å introspecte token med Texas")
    }
}

data class TokenIntrospectionResponse(
    val active: Boolean,
    val error: String? = null,
    val sub: String? = null,
    val aud: List<String>? = null,
    val exp: Long? = null,
    val iat: Long? = null,
)
