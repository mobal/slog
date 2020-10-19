package hu.netcode.slog.controller

import hu.netcode.slog.data.document.Post
import hu.netcode.slog.data.dto.input.PostDto
import hu.netcode.slog.service.PostService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping(
    produces = [MediaType.APPLICATION_JSON_VALUE],
    value = ["/api/posts"]
)
@Validated
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
    @ResponseStatus(value = HttpStatus.OK)
    fun findAllActive(
        @RequestParam(
            defaultValue = "1",
            name = "page",
            required = false
        )
        page: Int
    ): List<Post> {
        return postService.findAllActive(page)
    }

    /* @GetMapping(value = ["/{slug}"])
    @ResponseStatus(value = HttpStatus.OK)
    fun findBySlug(@PathVariable slug: String): Post {
        return postService.findBySlug(slug)
    }

    @PutMapping(value = ["/{slug}"])
    @ResponseStatus(value = HttpStatus.OK)
    fun update(@PathVariable slug: String, @RequestBody @Valid dto: PostDto) {
        return postService.update(dto, slug)
    } */
}
