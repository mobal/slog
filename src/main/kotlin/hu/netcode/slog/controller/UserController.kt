package hu.netcode.slog.controller

import hu.netcode.slog.data.dto.PostDto
import hu.netcode.slog.data.entity.User
import hu.netcode.slog.properties.PagingProperties
import hu.netcode.slog.service.UserService
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
    value = ["/api/users"]
)
class UserController(
    private val pagingProperties: PagingProperties,
    private val userService: UserService
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    fun findAll(): List<User> {
        return userService.findAll()
    }

    @GetMapping(value = ["/{username}"])
    @ResponseStatus(value = HttpStatus.OK)
    fun findById(@PathVariable username: String): User {
        return userService.findByUsername(username)
    }

    @PostMapping(value = ["/registration"])
    @ResponseStatus(value = HttpStatus.CREATED)
    fun registration(@RequestBody postDto: PostDto) {
    }
}
