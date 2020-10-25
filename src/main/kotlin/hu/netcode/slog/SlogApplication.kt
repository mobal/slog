package hu.netcode.slog

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@ConfigurationPropertiesScan
@SpringBootApplication
class SlogApplication

fun main(args: Array<String>) {
    runApplication<SlogApplication>(*args)
}
