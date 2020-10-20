package hu.netcode.slog.data.repository

import hu.netcode.slog.data.document.User
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface UserRepository : MongoRepository<User, Int> {
    fun deleteByUsername(username: String)

    fun findByDeletedAtIsNullAndActivationIsNull(): List<User>

    fun findByDeletedAtIsNullAndActivationIsNullAndUsername(username: String): Optional<User>
}
