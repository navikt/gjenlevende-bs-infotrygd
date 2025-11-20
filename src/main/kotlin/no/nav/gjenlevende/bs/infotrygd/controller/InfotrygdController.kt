package no.nav.gjenlevende.bs.infotrygd.controller

import no.nav.gjenlevende.bs.infotrygd.dto.VedtakPeriodeRequest
import no.nav.gjenlevende.bs.infotrygd.dto.VedtakPeriodeResponse
import no.nav.gjenlevende.bs.infotrygd.service.InfotrygdService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/infotrygd")
open class InfotrygdController(
    private val infotrygdService: InfotrygdService,
) {
    @PostMapping("/perioder")
    @PreAuthorize("hasRole('SAKSBEHANDLER') and hasRole('BESLUTTER') and hasRole('VEILEDER')")
    fun hentPerioderForPerson(
        @RequestBody request: VedtakPeriodeRequest,
    ): ResponseEntity<VedtakPeriodeResponse> {
        val perioder = infotrygdService.hentVedtakPerioder(request.personident)
        return ResponseEntity.ok(perioder)
    }
}
