package no.nav.gjenlevende.bs.infotrygd.service

import no.nav.gjenlevende.bs.infotrygd.repository.InfotrygdRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Service

@Service
class InfotrygdService(
    private val infotrygdRepository: InfotrygdRepository,
    @Value("\${APP_DATASOURCE_USERNAME_PATH}") private val username: String,
) : CommandLineRunner {
    private val logger = LoggerFactory.getLogger(javaClass)

    override fun run(vararg args: String) {
        logger.info("oracle username: $username")
        val test = infotrygdRepository.test()
        logger.info("Antall treff i db: ${test.size} - skal være 7")
    }
}
