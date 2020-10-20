package hu.netcode.slog.data.repository.event

import hu.netcode.slog.data.document.User
import org.slf4j.LoggerFactory
import org.springframework.data.rest.core.annotation.HandleAfterCreate
import org.springframework.data.rest.core.annotation.RepositoryEventHandler

@RepositoryEventHandler(value = [User::class])
class UserEventHandler {
    private val logger = LoggerFactory.getLogger(javaClass)

    @HandleAfterCreate
    fun handleAfterCreate(user: User) {
        logger.info("User {} successfully created", user)
    }
}
