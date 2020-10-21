package hu.netcode.slog.controller

import hu.netcode.slog.data.document.User
import hu.netcode.slog.data.dto.input.UserDto
import hu.netcode.slog.properties.PagingProperties
import hu.netcode.slog.service.UserService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.net.URI
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid

@RestController
@RequestMapping(
    produces = [MediaType.APPLICATION_JSON_VALUE],
    value = ["/api/users"]
)
@Validated
class UserController(
    private val pagingProperties: PagingProperties,
    private val userService: UserService
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @DeleteMapping(value = ["/{username}"])
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    fun delete(@PathVariable username: String) {
        userService.delete(username)
    }

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    fun findAll(): List<User> {
        return userService.findAll()
    }

    @GetMapping(value = ["/{username}"])
    @ResponseStatus(value = HttpStatus.OK)
    fun findByUsername(@PathVariable username: String): User {
        return userService.findByUsername(username)
    }

    @PostMapping(value = ["/registration"])
    @ResponseStatus(value = HttpStatus.CREATED)
    fun registration(@RequestBody userDto: UserDto, req: HttpServletRequest): ResponseEntity<Unit> {
        logger.info("User registration started {}", userDto)
        val user = userService.create(userDto)
        return ResponseEntity.created(URI("${req.requestURL}/${user.username}"))
            .build()
    }

    @PutMapping(value = ["/{username}"])
    @ResponseStatus(value = HttpStatus.OK)
    fun update(@PathVariable username: String, @RequestBody @Valid dto: UserDto) {
        userService.update(dto, username)
    }
}
