package hu.netcode.slog.service

import hu.netcode.slog.data.document.User
import hu.netcode.slog.data.dto.input.UserDto
import hu.netcode.slog.data.repository.UserRepository
import hu.netcode.slog.exception.DocumentNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.util.UUID
import kotlin.jvm.Throws

@Service
class UserService(
    private val bCryptPasswordEncoder: BCryptPasswordEncoder,
    private val userRepository: UserRepository
) {
    private companion object {
        const val ERROR_MESSAGE_USER_NOT_FOUND = "The requested user was not found"
    }

    private val logger = LoggerFactory.getLogger(javaClass)

    fun delete(username: String) {
        userRepository.deleteByUsername(username)
    }

    fun create(userDto: UserDto): User {
        val user = User(
            activation = UUID.randomUUID().toString(),
            email = userDto.email,
            name = userDto.name,
            password = bCryptPasswordEncoder.encode(userDto.password),
            username = userDto.username
        )
        userRepository.save(user)
        return user
    }

    fun findAll(): List<User> {
        return userRepository.findByDeletedAtIsNullAndActivationIsNull()
    }

    @Throws(exceptionClasses = [DocumentNotFoundException::class])
    fun findByUsername(username: String): User {
        val op = userRepository.findByDeletedAtIsNullAndActivationIsNullAndUsername(username)
        if (op.isPresent) {
            return op.get()
        } else {
            throw DocumentNotFoundException("$ERROR_MESSAGE_USER_NOT_FOUND: $username")
        }
    }

    @Throws(exceptionClasses = [DocumentNotFoundException::class])
    fun update(dto: UserDto, username: String) {
        val op = userRepository.findByDeletedAtIsNullAndActivationIsNullAndUsername(username)
        if (op.isPresent) {
            userRepository.save(
                op.get().apply {
                    email = dto.email
                    name = dto.name
                    this.username = dto.username
                    password = dto.password
                }
            )
        } else {
            throw DocumentNotFoundException("$ERROR_MESSAGE_USER_NOT_FOUND: $username")
        }
    }
}
