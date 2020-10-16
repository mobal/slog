package hu.netcode.slog.service

import hu.netcode.slog.data.dto.input.UserDto
import hu.netcode.slog.data.document.User
import hu.netcode.slog.data.repository.UserRepository
import hu.netcode.slog.exception.DocumentNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class UserService(
    private val bCryptPasswordEncoder: BCryptPasswordEncoder,
    private val userRepository: UserRepository
) {
    private val logger = LoggerFactory.getLogger(javaClass)

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

    fun findByUsername(username: String): User {
        val op = userRepository.findByDeletedAtIsNullAndActivationIsNullAndUsername(username)
        if (op.isPresent) {
            return op.get()
        } else {
            throw DocumentNotFoundException("The requested user was not found with the following username $username")
        }
    }
}
