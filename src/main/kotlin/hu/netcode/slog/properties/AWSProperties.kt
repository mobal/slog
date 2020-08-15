package hu.netcode.slog.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "amazon.aws")
data class AWSProperties(
    var accessKey: String? = null,
    var secretKey: String? = null
)
