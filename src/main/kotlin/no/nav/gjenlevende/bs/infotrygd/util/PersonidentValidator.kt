package no.nav.gjenlevende.bs.infotrygd.util

object PersonidentValidator {
    private val PERSONIDENT_REGEX = """[0-9]{11}""".toRegex()

    fun validerPersonident(personident: String) {
        if (personident.length != 11) {
            throw IllegalArgumentException("Ugyldig personident. Det må være 11 siffer, fikk ${personident.length} tegn")
        }
        if (!PERSONIDENT_REGEX.matches(personident)) {
            throw IllegalArgumentException("Ugyldig personident. Det kan kun inneholde tall")
        }
    }

    fun erGyldigPersonident(personident: String): Boolean = personident.length == 11 && PERSONIDENT_REGEX.matches(personident)
}
