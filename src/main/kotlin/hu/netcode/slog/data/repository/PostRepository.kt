package hu.netcode.slog.data.repository

import hu.netcode.slog.data.entity.Post
import java.util.Optional
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

@Repository
interface PostRepository : PagingAndSortingRepository<Post, Int> {
    fun findByVisibleTrueAndDeletedAtIsNull(pageable: Pageable): List<Post>

    fun findByIdAndVisibleTrueAndDeletedAtIsNull(id: Int): Optional<Post>
}
