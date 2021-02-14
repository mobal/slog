package hu.netcode.slog.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import hu.netcode.slog.data.document.User
import hu.netcode.slog.data.dto.input.UserDto
import hu.netcode.slog.exception.DocumentNotFoundException
import hu.netcode.slog.properties.PagingProperties
import hu.netcode.slog.service.ExceptionService
import hu.netcode.slog.service.UserService
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.just
import io.mockk.verifySequence
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
import java.lang.Exception

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
        const val ERROR_MESSAGE = "Error"
        const val URL = "/api/users"

        val dto = UserDto(
            email = "user@at.com",
            name = "User",
            username = "username",
            password = "pwd",
            confirmPassword = "pwd"
        )
        val user = User(
            id = "5fa2cdb867c66c800541dcb2",
            userId = "6f35345b-0621-42a9-8d27-63ea7946121d",
            displayName = "Display Name",
            email = "user@at.com",
            name = "User",
            password = "pwd",
            username = "username"
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

    @DisplayName(value = "UserController: Tests for function delete")
    @Nested
    @TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
    inner class Delete {
        private val url = "$URL/user"
        @Test
        fun `successfully delete user`() {
            every { userService.delete(any()) } just Runs
            mockMvc.delete(url) {
                accept = MediaType.APPLICATION_JSON
                with(csrf())
                with(oauth2Login())
            }.andExpect {
                status { isNoContent() }
            }
            verifySequence {
                userService.delete(any())
            }
        }

        @Test
        fun `fail to delete user because of an exception`() {
            every { userService.delete(any()) } throws Exception(ERROR_MESSAGE)
            mockMvc.delete(url) {
                accept = MediaType.APPLICATION_JSON
                with(csrf())
                with(oauth2Login())
            }.andExpect {
                status { isInternalServerError() }
            }
        }

        @Test
        fun `fail to delete user without login`() {
            mockMvc.delete(url) {
                accept = MediaType.APPLICATION_JSON
                with(csrf())
            }.andExpect {
                status { isForbidden() }
            }
        }
    }

    @DisplayName(value = "UserController: Tests for function findAll")
    @Nested
    @TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
    inner class FindAll {
        @Test
        fun `successfully find all user`() {
            every { userService.findAll() } returns listOf(user)
            mockMvc.get(URL) {
                accept = MediaType.APPLICATION_JSON
            }.andExpect {
                status { is2xxSuccessful() }
            }
        }

        @Test
        fun `successfully find all users but return is empty`() {
            every { userService.findAll() } returns emptyList()
            mockMvc.get(URL) {
                accept = MediaType.APPLICATION_JSON
            }.andExpect {
                status { is2xxSuccessful() }
            }
        }
    }

    @DisplayName(value = "UserController: Tests for function findByUsername")
    @Nested
    @TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
    inner class FindByUsername {
        private val url = "$URL/user"
        @Test
        fun `successfully find user by username`() {
            every { userService.findByUsername(any()) } returns user
            mockMvc.get(url) {
                accept = MediaType.APPLICATION_JSON
            }.andExpect {
                status { is2xxSuccessful() }
            }
        }

        @Test
        fun `fail to find user by username`() {
            every { userService.findByUsername(any()) } throws DocumentNotFoundException(ERROR_MESSAGE)
            mockMvc.get(url) {
                accept = MediaType.APPLICATION_JSON
            }.andExpect {
                status { isNotFound() }
            }
        }
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
                with(csrf())
                with(oauth2Login())
            }.andExpect {
                status { isCreated() }
            }
        }

        @Test
        fun `failed to create user because validation fails`() {
            val dto = UserDto(email = "", name = "", username = "", password = "", confirmPassword = "")
            mockMvc.post(url) {
                accept = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(dto)
                contentType = MediaType.APPLICATION_JSON
                with(csrf())
                with(oauth2Login())
            }.andExpect {
                status { isBadRequest() }
            }
        }

        @Test
        fun `failed to create user because of an exception`() {
            every { userService.create(any()) } throws Exception(ERROR_MESSAGE)
            mockMvc.post(url) {
                accept = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(dto)
                contentType = MediaType.APPLICATION_JSON
                with(csrf())
                with(oauth2Login())
            }.andExpect {
                status { isInternalServerError() }
            }
        }

        @Test
        fun `failed to create user without login`() {
            mockMvc.post(url) {
                accept = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(dto)
                contentType = MediaType.APPLICATION_JSON
                with(csrf())
            }.andExpect {
                status { isForbidden() }
            }
        }
    }

    @DisplayName(value = "UserController: Tests for function update")
    @Nested
    @TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
    inner class Update {
        private val url = "$URL/user"

        @Test
        fun `successfully update user`() {
            mockMvc.put(url) {
                accept = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(dto)
                contentType = MediaType.APPLICATION_JSON
                with(csrf())
                with(oauth2Login())
            }.andExpect {
                status { is2xxSuccessful() }
            }
        }

        @Test
        fun `failed to update user because validation fails`() {
            val dto = UserDto(email = "", name = "", username = "", password = "", confirmPassword = "")
            mockMvc.put(url) {
                accept = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(dto)
                contentType = MediaType.APPLICATION_JSON
                with(csrf())
                with(oauth2Login())
            }.andExpect {
                status { isBadRequest() }
            }
        }

        @Test
        fun `fail to update user because not found`() {
            every { userService.update(any(), any()) } throws DocumentNotFoundException(ERROR_MESSAGE)
            mockMvc.put(url) {
                accept = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(dto)
                contentType = MediaType.APPLICATION_JSON
                with(csrf())
                with(oauth2Login())
            }.andExpect {
                status { isNotFound() }
            }
        }

        @Test
        fun `fail to update user because of an exception`() {
            every { userService.update(any(), any()) } throws Exception(ERROR_MESSAGE)
            mockMvc.put(url) {
                accept = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(dto)
                contentType = MediaType.APPLICATION_JSON
                with(csrf())
                with(oauth2Login())
            }.andExpect {
                status { isInternalServerError() }
            }
        }

        @Test
        fun `fail to update user without login`() {
            mockMvc.put(url) {
                accept = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(dto)
                contentType = MediaType.APPLICATION_JSON
                with(csrf())
            }.andExpect {
                status { isForbidden() }
            }
        }
    }
}
