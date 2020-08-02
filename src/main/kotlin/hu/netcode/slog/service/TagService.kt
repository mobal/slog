package hu.netcode.slog.service

import hu.netcode.slog.data.entity.Tag
import hu.netcode.slog.data.repository.TagRepository
import javax.persistence.EntityNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class TagService(
    private val tagRepository: TagRepository
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    fun findAll(): List<Tag> {
        return tagRepository.findAll()
    }

    fun findById(id: Int): Tag {
        val op = tagRepository.findById(id)
        if (op.isPresent) {
            return op.get()
        } else {
            throw EntityNotFoundException("The requested tag was not found with id $id")
        }
    }
}
