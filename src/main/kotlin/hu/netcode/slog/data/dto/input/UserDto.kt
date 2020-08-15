package hu.netcode.slog.data.dto.input

import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty

data class UserDto(
    @Email
    val email: String,
    val name: String,
    @NotEmpty
    val username: String,
    @NotEmpty
    val password: String,
    @NotEmpty
    val confirmPassword: String
)
