package hu.netcode.slog

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.web.servlet.config.annotation.EnableWebMvc

@ConfigurationPropertiesScan
@EnableWebMvc
@SpringBootApplication
class SlogApplication

fun main(args: Array<String>) {
    runApplication<SlogApplication>(*args)
}
