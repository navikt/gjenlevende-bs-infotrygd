package no.nav.gjenlevende.bs.infotrygd.repository

import org.slf4j.LoggerFactory
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.time.LocalDate
import kotlin.collections.emptyMap

@Repository
class InfotrygdRepository(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    fun harStønad(
        personIdenter: Set<String>,
        kunAktive: Boolean = false,
        dagensDato: LocalDate = LocalDate.now(),
    ): List<String> {
        val values =
            MapSqlParameterSource()
                .addValue("personIdenter", personIdenter)
        val filter: String =
            if (kunAktive) {
                values.addValue("dagensDato", dagensDato)
                " AND nvl(S.DATO_OPPHOR,V.DATO_INNV_TOM) > :dagensDato "
            } else {
                ""
            }

        val result =
            jdbcTemplate.query(
                """
                SELECT L.PERSONNR
                  FROM T_BESLUT B
                WHERE L.PERSONNR IN (:personIdenter)
                  $filter
                GROUP BY L.personnr
            """,
                values,
            ) { resultSet, _ ->
                resultSet.getString("PERSONNR")
            }
        return result.toList()
    }

    fun test(): List<String> {
        val user: String? =
            jdbcTemplate.queryForObject(
                "SELECT USER FROM DUAL",
                emptyMap<String, Any>(),
                String::class.java,
            )
        val currentSchema: String? =
            jdbcTemplate.queryForObject(
                "SELECT SYS_CONTEXT('USERENV','CURRENT_SCHEMA') FROM DUAL",
                emptyMap<String, Any>(),
                String::class.java,
            )

        logger.info("DB USER: $user, CURRENT_SCHEMA: $currentSchema")

        val result =
            jdbcTemplate.query("select TEKST from INFOTRYGD_EBQ.T_GRADSTYPE") { resultSet, _ ->
                resultSet.getString("TEKST")
            }
        return result.toList()
    }
}
