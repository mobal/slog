package hu.netcode.slog.data.repository

import hu.netcode.slog.data.document.Post
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import java.time.ZonedDateTime
import java.util.Optional

@Repository
interface PostRepository : PagingAndSortingRepository<Post, Int> {
    fun deleteByMetaSlug(slug: String)

    fun findByVisibleTrueAndDeletedAtIsNullAndPublishedAtBefore(
        pageable: Pageable,
        threshold: ZonedDateTime
    ): List<Post>

    fun findByVisibleTrueAndDeletedAtIsNullAndPublishedAtBeforeAndMetaSlug(
        threshold: ZonedDateTime,
        slug: String
    ): Optional<Post>
}
