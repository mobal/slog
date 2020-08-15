package hu.netcode.slog.data.dto.input

import javax.validation.constraints.NotEmpty

data class PostDto(
    @NotEmpty
    val body: String,
    @NotEmpty
    val title: String
)
