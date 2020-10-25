package hu.netcode.slog.configuration

import com.github.slugify.Slugify
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SlugifyConfiguration {
    @Bean
    fun slugify(): Slugify {
        return Slugify()
    }
}
