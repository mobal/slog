package hu.netcode.slog.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "slog.paging")
data class PagingProperties(
    var size: Int = 0
)
