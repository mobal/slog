package hu.netcode.slog.data.dto.input

import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotEmpty

data class PostDto(
    @NotEmpty
    val body: String,
    @NotEmpty
    val title: String,
    @JsonProperty(value = "tags")
    val tagList: List<String>
)
