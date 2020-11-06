package hu.netcode.slog.data.dto.input

import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty

data class UserDto(
    @get:Email
    val email: String,
    val name: String,
    @get:NotEmpty
    val username: String,
    @get:NotEmpty
    val password: String,
    @get:NotEmpty
    val confirmPassword: String
)
