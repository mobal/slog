package hu.netcode.slog.controller

import hu.netcode.slog.data.document.Post
import hu.netcode.slog.data.dto.input.PostDto
import hu.netcode.slog.service.PostService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.apache.logging.log4j.LogManager
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.net.URI
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid

@RestController
@RequestMapping(
    produces = [MediaType.APPLICATION_JSON_VALUE],
    value = ["/api/posts"]
)
class PostController(
    private val postService: PostService
) {
    private val logger = LogManager.getLogger(javaClass)

    @Operation(
        description = "Create a new post",
        responses = [
            ApiResponse(
                content = [
                    Content(schema = Schema(hidden = true))
                ],
                description = "Created",
                responseCode = "201"
            ),
            ApiResponse(
                content = [
                    Content(schema = Schema(hidden = true))
                ],
                description = "Bad Request",
                responseCode = "400"
            ),
            ApiResponse(
                content = [
                    Content(schema = Schema(hidden = true))
                ],
                description = "Internal Server Error",
                responseCode = "500"
            )
        ]
    )
    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    fun create(@RequestBody @Valid dto: PostDto, req: HttpServletRequest): ResponseEntity<Unit> {
        val post = postService.save(dto)
        return ResponseEntity.created(URI("${req.requestURL}/${post.meta.slug}"))
            .build()
    }

    @DeleteMapping(value = ["/{slug}"])
    @Operation(
        description = "Delete the specified post",
        responses = [
            ApiResponse(
                content = [
                    Content(schema = Schema(hidden = true))
                ],
                description = "No Content",
                responseCode = "204"
            ),
            ApiResponse(
                content = [
                    Content(schema = Schema(hidden = true))
                ],
                description = "Not Found",
                responseCode = "404"
            ),
            ApiResponse(
                content = [
                    Content(schema = Schema(hidden = true))
                ],
                description = "Internal Server Error",
                responseCode = "500"
            )
        ]
    )
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    fun delete(@PathVariable slug: String) {
        postService.delete(slug)
    }

    @GetMapping
    @Operation(
        description = "Get all posts",
        responses = [
            ApiResponse(
                content = [
                    Content(schema = Schema(hidden = true))
                ],
                description = "OK",
                responseCode = "200"
            ),
            ApiResponse(
                content = [
                    Content(schema = Schema(hidden = true))
                ],
                description = "Internal Server Error",
                responseCode = "500"
            )
        ]
    )
    @ResponseStatus(value = HttpStatus.OK)
    fun findAll(
        @RequestParam(
            defaultValue = "1",
            name = "page",
            required = false
        )
        page: Int
    ): List<Post> {
        return postService.findAll(page)
    }

    @GetMapping(value = ["/{slug}"])
    @Operation(
        description = "Get the specified post",
        responses = [
            ApiResponse(
                content = [
                    Content(schema = Schema(hidden = true))
                ],
                description = "OK",
                responseCode = "200"
            ),
            ApiResponse(
                content = [
                    Content(schema = Schema(hidden = true))
                ],
                description = "Not Found",
                responseCode = "404"
            ),
            ApiResponse(
                content = [
                    Content(schema = Schema(hidden = true))
                ],
                description = "Internal Server Error",
                responseCode = "500"
            )
        ]
    )
    @ResponseStatus(value = HttpStatus.OK)
    fun findBySlug(@PathVariable slug: String): Post {
        return postService.findBySlug(slug)
    }

    @Operation(
        description = "Update the specified post",
        responses = [
            ApiResponse(
                content = [
                    Content(schema = Schema(hidden = true))
                ],
                description = "OK",
                responseCode = "200"
            ),
            ApiResponse(
                content = [
                    Content(schema = Schema(hidden = true))
                ],
                description = "Not Found",
                responseCode = "404"
            ),
            ApiResponse(
                content = [
                    Content(schema = Schema(hidden = true))
                ],
                description = "Internal Server Error",
                responseCode = "500"
            )
        ]
    )
    @PutMapping(value = ["/{slug}"])
    @ResponseStatus(value = HttpStatus.OK)
    fun update(@PathVariable slug: String, @RequestBody @Valid dto: PostDto) {
        postService.update(dto, slug)
    }
}
