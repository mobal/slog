package hu.netcode.slog

import com.github.slugify.Slugify
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@EnableJpaRepositories
@SpringBootApplication
class SlogApplication {
    @Bean
    fun bcryptPasswordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun slugify(): Slugify {
        return Slugify()
    }
}

fun main(args: Array<String>) {
    runApplication<SlogApplication>(*args)
}
