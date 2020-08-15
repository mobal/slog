package hu.netcode.slog.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "slog.paging")
data class PagingProperties(
    val size: Int
)
