package hu.netcode.slog.service

import hu.netcode.slog.data.document.Tag
import hu.netcode.slog.data.repository.TagRepository
import hu.netcode.slog.exception.DocumentNotFoundException
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
            throw DocumentNotFoundException("The requested tag was not found with id $id")
        }
    }
}
