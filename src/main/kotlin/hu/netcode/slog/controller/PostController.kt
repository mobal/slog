package hu.netcode.slog.controller

import hu.netcode.slog.data.entity.Post
import hu.netcode.slog.service.PostService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(
        produces = [MediaType.APPLICATION_JSON_VALUE],
        value = ["/api/posts"]
)
class PostController(
    private val postService: PostService
) {
    @GetMapping
    fun get(): List<String> {
        return emptyList()
    }

    @GetMapping(value = ["/{id}"])
    fun post(@PathVariable id: Int): Post {
        return postService.findById(id)
    }
}
