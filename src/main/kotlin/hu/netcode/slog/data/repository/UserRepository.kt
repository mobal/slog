package hu.netcode.slog.data.repository

import hu.netcode.slog.data.entity.User
import java.util.Optional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Int> {
    fun findByDeletedAtIsNullAndActivationIsNull(): List<User>

    fun findByDeletedAtIsNullAndActivationIsNullAndUsername(username: String): Optional<User>
}
