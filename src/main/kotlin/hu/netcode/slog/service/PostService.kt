package hu.netcode.slog.service

import com.github.slugify.Slugify
import hu.netcode.slog.data.dto.PostDto
import hu.netcode.slog.data.entity.Post
import hu.netcode.slog.data.repository.PostRepository
import javax.persistence.EntityNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class PostService(
    private val postRepository: PostRepository,
    private val slugify: Slugify
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    fun findAll(): List<Post> {
        return postRepository.findAll()
    }

    @Throws(EntityNotFoundException::class)
    fun findById(id: Int): Post {
        val optional = postRepository.findById(id)
        if (optional.isPresent) {
            return optional.get()
        } else {
            throw EntityNotFoundException("The requested post was not found with id $id")
        }
    }

    fun save(dto: PostDto) {
        //
    }
}
