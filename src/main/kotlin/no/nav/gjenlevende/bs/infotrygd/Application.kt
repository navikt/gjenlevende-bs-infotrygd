package no.nav.gjenlevende.bs.infotrygd

import org.slf4j.LoggerFactory
import org.slf4j.MarkerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class Application

private val logger = LoggerFactory.getLogger(Application::class.java)
private val teamLogsMarker = MarkerFactory.getMarker("TEAM_LOGS")

fun main(args: Array<String>) {
    System.setProperty("oracle.jdbc.fanEnabled", "false")
    runApplication<Application>(*args)

    // TODO: Fjern denne testloggen etter at team-logs er verifisert å fungere
    logger.info(teamLogsMarker, "Team-logs test: Applikasjonen startet opp med team-logs aktivert")
}
