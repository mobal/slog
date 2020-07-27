package hu.netcode.slog.service

import hu.netcode.slog.data.entity.Post
import hu.netcode.slog.data.repository.PostRepository
import java.time.ZonedDateTime
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class PostService(
    postRepository: PostRepository
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    fun findById(id: Int): Post {
        return Post(
            id = 1,
            author = "author",
            body = "",
            createdAt = ZonedDateTime.now()
        )
    }
}
