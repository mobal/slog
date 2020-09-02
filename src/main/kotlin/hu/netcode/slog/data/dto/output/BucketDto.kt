package hu.netcode.slog.data.dto.output

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.ZonedDateTime

data class BucketDto(
    @JsonProperty
    val name: String,
    @JsonProperty(value = "createdAt")
    val creationDate: ZonedDateTime
)
