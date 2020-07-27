package hu.netcode.slog

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@EnableJpaRepositories
@SpringBootApplication
class SlogApplication

fun main(args: Array<String>) {
    runApplication<SlogApplication>(*args)
}
