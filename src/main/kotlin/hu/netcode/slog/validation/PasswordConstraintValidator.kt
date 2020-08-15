package hu.netcode.slog.validation

import hu.netcode.slog.data.dto.input.UserDto
import hu.netcode.slog.validation.constraint.PasswordConstraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class PasswordConstraintValidator : ConstraintValidator<PasswordConstraint, UserDto> {
    override fun isValid(value: UserDto, context: ConstraintValidatorContext?): Boolean {
        return (value.password.isNotEmpty() && value.confirmPassword.isNotEmpty()) &&
                value.password == value.confirmPassword
    }
}
