package hu.netcode.slog.service

import com.github.slugify.Slugify
import hu.netcode.slog.data.dto.input.PostDto
import hu.netcode.slog.data.entity.Meta
import hu.netcode.slog.data.entity.Post
import hu.netcode.slog.data.repository.PostRepository
import hu.netcode.slog.properties.PagingProperties
import org.hibernate.Hibernate
import org.slf4j.LoggerFactory
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.time.ZonedDateTime
import javax.persistence.EntityNotFoundException
import javax.transaction.Transactional

@Service
class PostService(
    private val pagingProperties: PagingProperties,
    private val postRepository: PostRepository,
    private val slugify: Slugify
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    private fun incrementViews(post: Post, value: Int = 1) {
        post.meta.views += value
        postRepository.save(post)
    }

    @Transactional
    fun findAll(page: Int): List<Post> {
        return postRepository.findByDeletedAtIsNullAndVisibleTrue(
            PageRequest.of(page - 1, pagingProperties.size)
        )
    }

    @Transactional
    fun findAllActive(page: Int): List<Post> {
        val postList = postRepository.findByVisibleTrueAndDeletedAtIsNullAndPublishedAtIsNotNullAndPublishedAtBefore(
            PageRequest.of(page - 1, pagingProperties.size),
            ZonedDateTime.now()
        )
        postList.forEach { Hibernate.initialize(it.tagList) }
        return postList
    }

    @Throws(exceptionClasses = [EntityNotFoundException::class])
    @Transactional
    fun findBySlug(slug: String): Post {
        val op = postRepository.findByVisibleTrueAndDeletedAtIsNullAndPublishedAtIsNotNullAndPublishedAtBeforeAndMetaSlug(
            ZonedDateTime.now(),
            slug
        )
        if (op.isPresent) {
            val post = op.get()
            incrementViews(post)
            return post
        } else {
            throw EntityNotFoundException("The requested post was not found with slug $slug")
        }
    }

    fun save(dto: PostDto) {
        postRepository.save(
            Post(
                author = "user",
                body = dto.body,
                title = dto.title,
                meta = Meta(slug = slugify.slugify(dto.title)),
                tagList = emptyList()
            )
        )
    }
}
