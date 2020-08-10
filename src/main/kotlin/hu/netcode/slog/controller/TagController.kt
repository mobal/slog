package hu.netcode.slog.controller

import hu.netcode.slog.data.entity.Tag
import hu.netcode.slog.service.TagService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(
    produces = [MediaType.APPLICATION_JSON_VALUE],
    value = ["/api/tags"]
)
class TagController(
    private val tagService: TagService
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    fun findAll(): List<Tag> {
        return tagService.findAll()
    }

    @GetMapping(value = ["/{id}"])
    @ResponseStatus(value = HttpStatus.OK)
    fun findById(@PathVariable id: Int): Tag {
        return tagService.findById(id)
    }
}
