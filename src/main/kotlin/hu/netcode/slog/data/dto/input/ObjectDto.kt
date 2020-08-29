package hu.netcode.slog.data.dto.input

import com.fasterxml.jackson.annotation.JsonProperty
import java.io.InputStream
import javax.validation.constraints.NotEmpty

data class ObjectDto(
    @JsonProperty(value = "bucket")
    @NotEmpty
    val bucketName: String,
    @NotEmpty
    val data: InputStream,
    @JsonProperty(value = "name")
    @NotEmpty
    val key: String,
    @NotEmpty
    val mime: String
)
