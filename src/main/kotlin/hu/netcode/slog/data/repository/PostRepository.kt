package hu.netcode.slog.data.repository

import hu.netcode.slog.data.entity.Post
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import java.time.ZonedDateTime
import java.util.Optional

@Repository
interface PostRepository : PagingAndSortingRepository<Post, Int> {
    fun findByVisibleTrueAndDeletedAtIsNullAndPublishedAtIsNotNullAndPublishedAtBefore(
        pageable: Pageable,
        threshold: ZonedDateTime
    ): List<Post>

    fun findByDeletedAtIsNullAndVisibleTrue(pageable: Pageable): List<Post>

    fun findByMetaSlug(slug: String): Optional<Post>

    fun findByVisibleTrueAndDeletedAtIsNullAndPublishedAtIsNotNullAndPublishedAtBeforeAndMetaSlug(
        threshold: ZonedDateTime,
        slug: String
    ): Optional<Post>
}
