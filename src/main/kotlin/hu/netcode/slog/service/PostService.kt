package hu.netcode.slog.service

import hu.netcode.slog.data.entity.Post
import hu.netcode.slog.data.repository.PostRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import javax.persistence.EntityNotFoundException

@Service
class PostService(
    private val postRepository: PostRepository
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
}
