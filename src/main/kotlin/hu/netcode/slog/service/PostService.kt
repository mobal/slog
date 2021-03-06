package hu.netcode.slog.service

import com.github.slugify.Slugify
import hu.netcode.slog.data.document.Meta
import hu.netcode.slog.data.document.Post
import hu.netcode.slog.data.dto.input.PostDto
import hu.netcode.slog.data.repository.PostRepository
import hu.netcode.slog.exception.DocumentNotFoundException
import hu.netcode.slog.properties.PagingProperties
import org.apache.logging.log4j.LogManager
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.time.ZonedDateTime
import java.util.StringTokenizer
import kotlin.jvm.Throws
import kotlin.math.ceil

@Service
class PostService(
    private val pagingProperties: PagingProperties,
    private val postRepository: PostRepository,
    private val slugify: Slugify
) {
    private companion object {
        const val ERROR_MESSAGE_POST_NOT_FOUND = "The requested post was not found"
        const val WORDS_PER_MINUTE = 200
    }

    private val logger = LogManager.getLogger(javaClass)

    private fun calculateReadingTime(body: String): Int {
        return ceil((StringTokenizer(body).countTokens() / 200).toDouble()).toInt()
    }

    fun delete(slug: String) {
        return postRepository.deleteByMetaSlug(slug)
    }

    fun findAll(page: Int): List<Post> {
        return postRepository.findByVisibleTrueAndDeletedAtIsNullAndPublishedAtBefore(
            PageRequest.of(page - 1, pagingProperties.size),
            ZonedDateTime.now()
        )
    }

    @Throws(exceptionClasses = [DocumentNotFoundException::class])
    fun findBySlug(slug: String): Post {
        val op = postRepository.findByVisibleTrueAndDeletedAtIsNullAndPublishedAtBeforeAndMetaSlug(
            ZonedDateTime.now(),
            slug
        )
        if (op.isPresent) {
            return op.get()
        } else {
            throw DocumentNotFoundException("$ERROR_MESSAGE_POST_NOT_FOUND: $slug")
        }
    }

    fun save(dto: PostDto): Post {
        return postRepository.save(
            Post(
                author = "user",
                body = dto.body,
                meta = Meta(
                    readingTime = calculateReadingTime(dto.body),
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
            ZonedDateTime.now(),
            slug
        )
        if (op.isPresent) {
            postRepository.save(
                op.get().apply {
                    author = dto.author
                    body = dto.body
                    tagList = dto.tagList
                    title = dto.title
                    updatedAt = ZonedDateTime.now()
                }
            )
        } else {
            throw DocumentNotFoundException("$ERROR_MESSAGE_POST_NOT_FOUND: $slug")
        }
    }
}
