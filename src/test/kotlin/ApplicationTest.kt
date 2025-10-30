import org.springframework.boot.builder.SpringApplicationBuilder

class ApplicationTest

fun main(args: Array<String>) {
    SpringApplicationBuilder(ApplicationTest::class.java)
        .profiles(
            "test",
        ).run(*args)
}
