package hu.netcode.slog.data.dto.input

import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotEmpty

data class PostDto(
    @get:NotEmpty
    val author: String,
    @get:NotEmpty
    val body: String,
    @get:NotEmpty
    val title: String,
    @JsonProperty(value = "tags")
    @get:NotEmpty
    val tagList: List<String>
)
