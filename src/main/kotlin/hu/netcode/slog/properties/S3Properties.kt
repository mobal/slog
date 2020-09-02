package hu.netcode.slog.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "amazon.aws.s3")
data class S3Properties(
    val accessKey: String,
    val host: String,
    val port: String,
    val region: String,
    val secretKey: String
)
