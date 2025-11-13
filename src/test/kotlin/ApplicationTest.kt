import no.nav.gjenlevende.bs.infotrygd.Application
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest(
    classes = [Application::class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
)
@ActiveProfiles("test")
class ApplicationTest {
    //@Test disables frem til testing av sql går fint
    fun contextLoads() {
        // Her starter Spring Boot en web-server på en tilfeldig port
    }
}
