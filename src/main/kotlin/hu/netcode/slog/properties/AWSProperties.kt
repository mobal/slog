package hu.netcode.slog.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "amazon.aws")
data class AWSProperties(
    val accessKey: String,
    val region: String,
    val secretKey: String
)
