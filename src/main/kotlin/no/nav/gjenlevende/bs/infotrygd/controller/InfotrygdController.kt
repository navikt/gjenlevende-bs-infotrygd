package no.nav.gjenlevende.bs.infotrygd.controller

import no.nav.gjenlevende.bs.infotrygd.dto.VedtakPeriodeResponse
import no.nav.gjenlevende.bs.infotrygd.service.InfotrygdService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/infotrygd")
open class InfotrygdController(
    private val infotrygdService: InfotrygdService,
) {
    @GetMapping("/perioder/{personident}")
    @PreAuthorize("hasRole('SAKSBEHANDLER') and hasRole('BESLUTTER') and hasRole('VEILEDER')")
    fun hentPerioderForPerson(
        @PathVariable personident: String,
    ): ResponseEntity<VedtakPeriodeResponse> {
        val perioder = infotrygdService.hentVedtakPerioder(personident)
        return ResponseEntity.ok(perioder)
    }
}
