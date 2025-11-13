package no.nav.gjenlevende.bs.infotrygd.repository

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class InfotrygdRepository(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
) {
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
        val result =
            jdbcTemplate.query(
                """
                select TEKST from T_GRADSTYPE;
                """,
            ) { resultSet, _ ->
                resultSet.getString("TEKST")
            }
        return result.toList()
    }
}
