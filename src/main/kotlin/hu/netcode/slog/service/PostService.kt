package hu.netcode.slog.service

import com.github.slugify.Slugify
import hu.netcode.slog.data.document.Meta
import hu.netcode.slog.data.document.Post
import hu.netcode.slog.data.dto.input.PostDto
import hu.netcode.slog.data.repository.PostRepository
import hu.netcode.slog.exception.DocumentNotFoundException
import hu.netcode.slog.properties.PagingProperties
import org.slf4j.LoggerFactory
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import kotlin.jvm.Throws

@Service
class PostService(
    private val pagingProperties: PagingProperties,
    private val postRepository: PostRepository,
    private val slugify: Slugify
) {
    private companion object {
        const val ERROR_MESSAGE_POST_NOT_FOUND = "The requested post was not found"
    }

    private val logger = LoggerFactory.getLogger(javaClass)

    fun delete(slug: String) {
        return postRepository.deleteByMetaSlug(slug)
    }

    fun findAll(page: Int): List<Post> {
        return postRepository.findByVisibleTrueAndDeletedAtIsNullAndPublishedAtBefore(
            PageRequest.of(page - 1, pagingProperties.size),
            LocalDateTime.now()
        )
    }

    @Throws(exceptionClasses = [DocumentNotFoundException::class])
    fun findBySlug(slug: String): Post {
        val op = postRepository.findByVisibleTrueAndDeletedAtIsNullAndPublishedAtBeforeAndMetaSlug(
            LocalDateTime.now(),
            slug
        )
        if (op.isPresent) {
            return op.get()
        } else {
            throw DocumentNotFoundException("$ERROR_MESSAGE_POST_NOT_FOUND:$slug")
        }
    }

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

    @Throws(exceptionClasses = [DocumentNotFoundException::class])
    fun update(dto: PostDto, slug: String) {
        val op = postRepository.findByVisibleTrueAndDeletedAtIsNullAndPublishedAtBeforeAndMetaSlug(
            LocalDateTime.now(),
            slug
        )
        if (op.isPresent) {
            postRepository.save(
                op.get().apply {
                    author = dto.author
                    body = dto.body
                    tagList = dto.tagList
                    title = dto.title
                }
            )
        } else {
            throw DocumentNotFoundException("$ERROR_MESSAGE_POST_NOT_FOUND:$slug")
        }
    }
}
