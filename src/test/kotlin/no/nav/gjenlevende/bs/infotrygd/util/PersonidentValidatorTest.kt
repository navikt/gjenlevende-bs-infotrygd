package no.nav.gjenlevende.bs.infotrygd.util

import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class PersonidentValidatorTest {
    @Test
    fun `validerPersonident skal godta gyldig 11-sifret personident`() {
        assertDoesNotThrow {
            PersonidentValidator.validerPersonident("12345678901")
        }
    }

    @Test
    fun `validerPersonident skal kaste exception for personident som er for kort`() {
        val exception =
            assertThrows(IllegalArgumentException::class.java) {
                PersonidentValidator.validerPersonident("1234567890")
            }
        assertEquals("Ugyldig personident. Det må være 11 siffer, fikk 10 tegn", exception.message)
    }

    @Test
    fun `validerPersonident skal kaste exception for personident som er for lang`() {
        val exception =
            assertThrows(IllegalArgumentException::class.java) {
                PersonidentValidator.validerPersonident("123456789012")
            }
        assertEquals("Ugyldig personident. Det må være 11 siffer, fikk 12 tegn", exception.message)
    }

    @Test
    fun `validerPersonident skal kaste exception for personident med bokstaver`() {
        val exception =
            assertThrows(IllegalArgumentException::class.java) {
                PersonidentValidator.validerPersonident("1234567890a")
            }
        assertEquals("Ugyldig personident. Det kan kun inneholde tall", exception.message)
    }

    @Test
    fun `validerPersonident skal kaste exception for personident med spesialtegn`() {
        val exception =
            assertThrows(IllegalArgumentException::class.java) {
                PersonidentValidator.validerPersonident("12345678-01")
            }
        assertEquals("Ugyldig personident. Det kan kun inneholde tall", exception.message)
    }

    @Test
    fun `validerPersonident skal kaste exception for tom streng`() {
        val exception =
            assertThrows(IllegalArgumentException::class.java) {
                PersonidentValidator.validerPersonident("")
            }
        assertEquals("Ugyldig personident. Det må være 11 siffer, fikk 0 tegn", exception.message)
    }

    @Test
    fun `validerPersonident skal kaste exception for streng med kun tekst`() {
        val exception =
            assertThrows(IllegalArgumentException::class.java) {
                PersonidentValidator.validerPersonident("test")
            }
        assertEquals("Ugyldig personident. Det må være 11 siffer, fikk 4 tegn", exception.message)
    }

    @Test
    fun `erGyldigPersonident skal returnere true for gyldig personident`() {
        assertTrue(PersonidentValidator.erGyldigPersonident("12345678901"))
    }

    @Test
    fun `erGyldigPersonident skal returnere false for ugyldig lengde`() {
        assertFalse(PersonidentValidator.erGyldigPersonident("1234567890"))
        assertFalse(PersonidentValidator.erGyldigPersonident("123456789012"))
    }

    @Test
    fun `erGyldigPersonident skal returnere false for ikke-numerisk innhold`() {
        assertFalse(PersonidentValidator.erGyldigPersonident("1234567890a"))
        assertFalse(PersonidentValidator.erGyldigPersonident("test"))
        assertFalse(PersonidentValidator.erGyldigPersonident("12345678-01"))
    }
}
