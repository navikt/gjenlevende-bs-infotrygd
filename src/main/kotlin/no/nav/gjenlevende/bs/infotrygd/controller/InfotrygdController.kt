package no.nav.gjenlevende.bs.infotrygd.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/infotrygd")
class InfotrygdController {

    @GetMapping("/ping")
    fun ping(): ResponseEntity<String> = ResponseEntity.ok("Pong fra gjenlevende-bs-infotrygd")
}