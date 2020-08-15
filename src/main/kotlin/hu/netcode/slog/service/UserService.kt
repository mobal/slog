package hu.netcode.slog.service

import hu.netcode.slog.data.dto.UserDto
import hu.netcode.slog.data.entity.User
import hu.netcode.slog.data.repository.UserRepository
import java.util.UUID
import javax.persistence.EntityNotFoundException
import javax.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

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
        return userRepository.findAll()
    }

    @Transactional
    fun findByUsername(username: String): User {
        val op = userRepository.findByUsernameAndActivationIsNull(username)
        if (op.isPresent) {
            return op.get()
        } else {
            throw EntityNotFoundException("The requested user was not found with the following username $username")
        }
    }
}
