package hu.netcode.slog.validation.constraint

import hu.netcode.slog.validation.PasswordConstraintValidator
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@Constraint(validatedBy = [PasswordConstraintValidator::class])
@MustBeDocumented
@Target(allowedTargets = [AnnotationTarget.FIELD])
annotation class PasswordConstraint(
    val message: String = "Password and confirmation password does not match",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
