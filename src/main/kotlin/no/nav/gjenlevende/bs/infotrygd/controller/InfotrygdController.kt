package no.nav.gjenlevende.bs.infotrygd.controller

import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/infotrygd")
class InfotrygdController {

    @GetMapping("/ping")
    @PreAuthorize("hasRole('SAKSBEHANDLER')")
    fun ping(
        @AuthenticationPrincipal jwt: Jwt,
    ): ResponseEntity<Map<String, String>> {
        val navIdent = jwt.getClaimAsString("NAVident")

        return ResponseEntity.ok(
            mapOf(
                "melding" to "Pong fra gjenlevende-bs-infotrygd",
                "navIdent" to navIdent,
            ),
        )
    }
}