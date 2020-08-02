package hu.netcode.slog.data.repository

import hu.netcode.slog.data.entity.Post
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface PostRepository : JpaRepository<Post, Int> {
    fun findByVisibleTrueAndDeletedAtIsNull(): List<Post>

    fun findByIdAndVisibleTrueAndDeletedAtIsNull(id: Int): Optional<Post>
}
