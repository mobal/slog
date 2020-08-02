package hu.netcode.slog.controller

import hu.netcode.slog.data.dto.PostDto
import hu.netcode.slog.data.entity.Post
import hu.netcode.slog.service.PostService
import javax.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(
    produces = [MediaType.APPLICATION_JSON_VALUE],
    value = ["/api/posts"]
)
class PostController(
    private val postService: PostService
) {
    private val logger = LoggerFactory.getLogger(javaClass)
    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    fun create(@RequestBody @Valid dto: PostDto) {
        postService.save(dto)
    }

    @GetMapping
    fun findAll(): List<Post> {
        return postService.findAll()
    }

    @GetMapping(value = ["/{id}"])
    fun findById(@PathVariable id: Int): Post {
        return postService.findById(id)
    }
}
