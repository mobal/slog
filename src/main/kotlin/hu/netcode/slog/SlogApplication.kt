package hu.netcode.slog

import com.github.slugify.Slugify
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@EnableJpaRepositories
@SpringBootApplication
class SlogApplication {
    @Bean
    fun slugify(): Slugify {
        return Slugify()
    }
}

fun main(args: Array<String>) {
    runApplication<SlogApplication>(*args)
}
