package no.nav.gjenlevende.bs.infotrygd.infrastruktur.exception

import org.springframework.http.HttpStatus

inline fun brukerfeilHvis(
    betingelse: Boolean,
    httpStatus: HttpStatus = HttpStatus.BAD_REQUEST,
    lazyMessage: () -> String,
) {
    if (betingelse) {
        throw ApiFeil(lazyMessage(), httpStatus)
    }
}

inline fun brukerfeilHvisIkke(
    betingelse: Boolean,
    httpStatus: HttpStatus = HttpStatus.BAD_REQUEST,
    lazyMessage: () -> String,
) {
    if (!betingelse) {
        throw ApiFeil(lazyMessage(), httpStatus)
    }
}
