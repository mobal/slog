package hu.netcode.slog.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

@TestConfiguration
class ObjectMapperConfiguration {
    @Bean
    fun objectMapper(): ObjectMapper {
        return jacksonObjectMapper().registerModule(JavaTimeModule())
    }
}
