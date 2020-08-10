package hu.netcode.slog.service

import hu.netcode.slog.data.entity.User
import hu.netcode.slog.data.repository.UserRepository
import javax.persistence.EntityNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    fun findAll(): List<User> {
        return userRepository.findAll()
    }

    fun findByUsername(username: String): User {
        val op = userRepository.findByUsername(username)
        if (op.isPresent) {
            return op.get()
        } else {
            throw EntityNotFoundException("The requested user was not found with the following username $username")
        }
    }
}
