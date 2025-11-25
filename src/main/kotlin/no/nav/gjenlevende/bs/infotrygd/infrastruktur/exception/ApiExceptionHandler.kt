package no.nav.gjenlevende.bs.infotrygd.infrastruktur.exception

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.resource.NoResourceFoundException

@ControllerAdvice
class ApiExceptionHandler {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @ExceptionHandler(ApiFeil::class)
    fun handleApiFeil(feil: ApiFeil): ResponseEntity<FeilResponse> {
        logger.warn("ApiFeil: ${feil.feilmelding}", feil)
        return ResponseEntity
            .status(feil.httpStatus)
            .body(FeilResponse(feil.feilmelding, feil.httpStatus.value()))
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(e: IllegalArgumentException): ResponseEntity<FeilResponse> {
        logger.warn("IllegalArgumentException: ${e.message}", e)
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(FeilResponse(e.message ?: "Ugyldig request", HttpStatus.BAD_REQUEST.value()))
    }

    @ExceptionHandler(NoResourceFoundException::class)
    fun handleNoResourceFoundException(e: NoResourceFoundException): ResponseEntity<FeilResponse> {
        val ressursPath = e.resourcePath

        if (ressursPath.startsWith("/internal/")) {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(FeilResponse("Ressurs ikke funnet", HttpStatus.NOT_FOUND.value()))
        }

        logger.debug("Ressurs ikke funnet: $ressursPath")
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(FeilResponse("Ressurs ikke funnet", HttpStatus.NOT_FOUND.value()))
    }

    @ExceptionHandler(Exception::class)
    fun handleGeneralException(e: Exception): ResponseEntity<FeilResponse> {
        if (e.javaClass.packageName.startsWith("org.springframework")) {
            throw e
        }

        logger.error("Uventet feil: ${e.message}", e)
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(FeilResponse("En uventet feil oppstod", HttpStatus.INTERNAL_SERVER_ERROR.value()))
    }
}

data class FeilResponse(
    val melding: String,
    val status: Int,
)
