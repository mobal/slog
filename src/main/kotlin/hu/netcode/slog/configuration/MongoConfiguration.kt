package hu.netcode.slog.configuration

import com.github.cloudyrock.spring.v5.EnableMongock
import hu.netcode.slog.converter.DateToZonedDateTimeConverter
import hu.netcode.slog.converter.ZonedDateTimeToDateConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.core.convert.MongoCustomConversions
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@Configuration
@EnableMongock
@EnableMongoRepositories(
    value = [
        "hu.netcode.slog.data.repository"
    ]
)
class MongoConfiguration {
    @Bean
    fun mongoCustomConversions(): MongoCustomConversions {
        return MongoCustomConversions(listOf(DateToZonedDateTimeConverter(), ZonedDateTimeToDateConverter()))
    }
}
