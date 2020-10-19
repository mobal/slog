package hu.netcode.slog.service

import com.github.slugify.Slugify
import hu.netcode.slog.data.document.Meta
import hu.netcode.slog.data.dto.input.PostDto
import hu.netcode.slog.data.document.Post
import hu.netcode.slog.data.repository.PostRepository
import hu.netcode.slog.exception.DocumentNotFoundException
import hu.netcode.slog.properties.PagingProperties
import org.slf4j.LoggerFactory
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.time.ZonedDateTime

@Service
class PostService(
    private val pagingProperties: PagingProperties,
    private val postRepository: PostRepository,
    private val slugify: Slugify
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    fun findAll(page: Int): List<Post> {
        return postRepository.findByDeletedAtIsNullAndVisibleTrue(
            PageRequest.of(page - 1, pagingProperties.size)
        )
    }

    fun findAllActive(page: Int): List<Post> {
        val postList = postRepository.findByVisibleTrueAndDeletedAtIsNullAndPublishedAtIsNotNullAndPublishedAtBefore(
            PageRequest.of(page - 1, pagingProperties.size),
            ZonedDateTime.now()
        )
        // postList.forEach { Hibernate.initialize(it.tagList) }
        return postList
    }

    /* @Throws(exceptionClasses = [DocumentNotFoundException::class])
    fun findBySlug(slug: String): Post {
        val op = postRepository.findByVisibleTrueAndDeletedAtIsNullAndPublishedAtIsNotNullAndPublishedAtBeforeAndMetaSlug(
            ZonedDateTime.now(),
            slug
        )
        if (op.isPresent) {
            val post = op.get()
            return post
        } else {
            throw DocumentNotFoundException("The requested post was not found with slug $slug")
        }
    } */

    fun save(dto: PostDto) {
        postRepository.save(
            Post(
                author = "user",
                body = dto.body,
                meta = Meta(
                    slug = slugify.slugify(dto.title)
                ),
                tagList = dto.tagList,
                title = dto.title
            )
        )
    }

    /* fun update(dto: PostDto, slug: String) {
        val op = postRepository.findByMetaSlug(slug)
        if (op.isPresent) {
            //
        } else {
            throw DocumentNotFoundException("The requested post was not found with slug $slug")
        }
    } */
}
