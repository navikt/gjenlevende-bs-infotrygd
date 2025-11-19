package no.nav.gjenlevende.bs.infotrygd.service

import no.nav.gjenlevende.bs.infotrygd.dto.BarnInfo
import no.nav.gjenlevende.bs.infotrygd.dto.PeriodeResponse
import no.nav.gjenlevende.bs.infotrygd.dto.StønadType
import no.nav.gjenlevende.bs.infotrygd.dto.VedtakPeriodeResponse
import no.nav.gjenlevende.bs.infotrygd.repository.InfotrygdRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class InfotrygdService(
    private val infotrygdRepository: InfotrygdRepository,
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    fun test() {
        val test = infotrygdRepository.test()
        logger.info("Antall treff i db: ${test.size} - skal være 7")
    }

    fun hentVedtakPerioder(personident: String): VedtakPeriodeResponse {
        logger.info("Henter vedtak perioder for person")

        val vedtakPerioder =
            infotrygdRepository
                .hentVedtakPerioderForPerson(personident)
                .filter { it.erGyldigPeriode() }

        if (vedtakPerioder.isEmpty()) {
            logger.info("Ingen vedtak perioder funnet for person")
            return VedtakPeriodeResponse(personident = personident)
        }

        val vedtakIder = vedtakPerioder.map { it.vedtakId }

        val roller = infotrygdRepository.hentRollerForVedtak(vedtakIder)
        val rollerPerVedtak = roller.groupBy { it.vedtakId }

        val barnetilsynVedtak = vedtakPerioder.filter { it.stønadType == StønadType.BARNETILSYN }
        val skolepengerVedtak = vedtakPerioder.filter { it.stønadType == StønadType.SKOLEPENGER }

        val barnetilsynPerioder =
            barnetilsynVedtak.map { vedtak ->
                val barnForVedtak = rollerPerVedtak[vedtak.vedtakId].orEmpty()
                val vedtakTom = vedtak.beregnTomDato()

                val harAvvikendeDatoer =
                    barnForVedtak.any { barn ->
                        barn.fom != vedtak.datoFom || barn.tom != vedtakTom
                    }

                PeriodeResponse(
                    fom = vedtak.datoFom,
                    tom = vedtakTom,
                    vedtakId = vedtak.vedtakId,
                    stønadId = vedtak.stønadId,
                    barnPersonLøpenummer = barnForVedtak.map { it.personLøpenummer },
                    barnDetaljer =
                        if (harAvvikendeDatoer) {
                            barnForVedtak.map { barn ->
                                BarnInfo(
                                    personLøpenummer = barn.personLøpenummer,
                                    fom = barn.fom,
                                    tom = barn.tom,
                                )
                            }
                        } else {
                            null
                        },
                )
            }

        val skolepengerPerioder =
            skolepengerVedtak.map { vedtak ->
                val barnForVedtak = rollerPerVedtak[vedtak.vedtakId].orEmpty()
                val vedtakTom = vedtak.beregnTomDato()

                // Sjekk om noen barn har andre datoer enn vedtaket
                val harAvvikendeDatoer =
                    barnForVedtak.any { barn ->
                        barn.fom != vedtak.datoFom || barn.tom != vedtakTom
                    }

                PeriodeResponse(
                    fom = vedtak.datoFom,
                    tom = vedtakTom,
                    vedtakId = vedtak.vedtakId,
                    stønadId = vedtak.stønadId,
                    barnPersonLøpenummer = barnForVedtak.map { it.personLøpenummer },
                    barnDetaljer =
                        if (harAvvikendeDatoer) {
                            barnForVedtak.map { barn ->
                                BarnInfo(
                                    personLøpenummer = barn.personLøpenummer,
                                    fom = barn.fom,
                                    tom = barn.tom,
                                )
                            }
                        } else {
                            null
                        },
                )
            }

        logger.info("Fant ${barnetilsynPerioder.size} barnetilsyn-perioder og ${skolepengerPerioder.size} skolepenger-perioder")

        return VedtakPeriodeResponse(
            personident = personident,
            barnetilsyn = barnetilsynPerioder,
            skolepenger = skolepengerPerioder,
        )
    }
}
