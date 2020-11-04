package hu.netcode.slog.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import hu.netcode.slog.data.dto.input.UserDto
import hu.netcode.slog.properties.PagingProperties
import hu.netcode.slog.service.ExceptionService
import hu.netcode.slog.service.UserService
import io.mockk.clearAllMocks
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@AutoConfigureMockMvc
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
@WebMvcTest(
    value = [
        ExceptionService::class,
        UserController::class
    ]
)
class UserControllerTest {
    private companion object {
        const val URL = "/api/users"

        val dto = UserDto(
            email = "user@at.com",
            name = "User",
            username = "username",
            password = "pwd",
            confirmPassword = "pwd"
        )
    }

    @Autowired
    private lateinit var exceptionService: ExceptionService
    @Autowired
    private lateinit var mockMvc: MockMvc
    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockkBean(relaxed = true)
    private lateinit var pagingProperties: PagingProperties
    @MockkBean(relaxed = true)
    private lateinit var userService: UserService

    @BeforeEach
    fun init() {
        clearAllMocks()
    }

    @DisplayName(value = "UserController: Tests for function register")
    @Nested
    @TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
    inner class Register {
        private val url = "$URL/register"

        @Test
        fun `successfully create user`() {
            mockMvc.post(url) {
                accept = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(dto)
                contentType = MediaType.APPLICATION_JSON
            }.andExpect {
                status { isCreated }
            }
        }

        @Test
        fun `failed to create user because validation fails`() {
            val dto = UserDto(email = "", name = "", username = "", password = "", confirmPassword = "")
            mockMvc.post(url) {
                accept = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(dto)
                contentType = MediaType.APPLICATION_JSON
            }.andExpect {
                status { isBadRequest }
            }
        }
    }
}
