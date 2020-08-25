package hu.netcode.slog.data.dto.output

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.ZonedDateTime

data class S3ObjectDto(
    @JsonProperty(value = "name")
    val key: String,
    @JsonProperty
    val size: Long,
    @JsonProperty(value = "modifiedAt")
    val lastModified: ZonedDateTime
)
