package hu.netcode.slog.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.logging.log4j.LogManager
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@Profile(value = ["dev"])
@RestController
@RequestMapping(
    produces = [MediaType.APPLICATION_JSON_VALUE],
    value = ["/api/auth"]
)
class AuthController(
    private val objectMapper: ObjectMapper
) {
    private val logger = LogManager.getLogger(javaClass)

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    fun getAuthenticatedUser(@AuthenticationPrincipal oAuth2User: OAuth2User): String {
        return objectMapper.writeValueAsString(oAuth2User)
    }
}
