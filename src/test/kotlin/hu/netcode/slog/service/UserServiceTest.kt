package hu.netcode.slog.service

import com.ninjasquad.springmockk.MockkBean
import hu.netcode.slog.data.document.User
import hu.netcode.slog.data.dto.input.UserDto
import hu.netcode.slog.data.repository.UserRepository
import hu.netcode.slog.exception.DocumentNotFoundException
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.just
import io.mockk.verifySequence
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.time.LocalDateTime
import java.util.Optional

@SpringBootTest(
    classes = [UserService::class]
)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserServiceTest {
    private companion object {
        val dto = UserDto(
            email = "user@gmail.com",
            name = "User",
            username = "username",
            password = "passwd",
            confirmPassword = "passwd"
        )
        val user = User(
            userId = "6724f5c7-cb17-40b8-b41f-67bfefbcf851",
            displayName = "User",
            email = "user@gmail.com",
            name = "User",
            password = "passwd",
            username = "username",
            createdAt = LocalDateTime.now()
        )
    }

    @Autowired
    private lateinit var userService: UserService

    @MockkBean(relaxed = true)
    private lateinit var bCryptPasswordEncoder: BCryptPasswordEncoder
    @MockkBean(relaxed = true)
    private lateinit var userRepository: UserRepository

    @BeforeEach
    fun init() {
        clearAllMocks()
    }

    @DisplayName(value = "Tests for function create")
    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class Create {
        @Test
        fun `successfully create user`() {
            every { bCryptPasswordEncoder.encode(any()) } returns "passwd"
            every { userRepository.save(any()) } returns user
            val result = userService.create(dto)
            verifySequence {
                bCryptPasswordEncoder.encode(any())
                userRepository.save(any())
            }
            assertEquals(user.email, result.email)
            assertEquals(user.name, result.name)
            assertEquals(user.password, result.password)
            assertEquals(user.username, result.username)
        }

        fun `fail to create user`() {
            // TODO
        }
    }

    @DisplayName(value = "Tests for function delete")
    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class Delete {
        @Test
        fun `successfully delete user`() {
            every { userRepository.deleteByUsername(any()) } just Runs
            userService.delete("user")
            verifySequence {
                userRepository.deleteByUsername(any())
            }
        }
    }

    @DisplayName(value = "Tests for function findAll")
    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class FindAll {
        @Test
        fun `successfully find all users`() {
            every { userRepository.findByDeletedAtIsNullAndActivationIsNull() } returns listOf(user)
            val userList = userService.findAll()
            assertEquals(1, userList.size)
            assertEquals(user, userList[0])
        }

        @Test
        fun `successfully find all users but result is empty`() {
            every { userRepository.findByDeletedAtIsNullAndActivationIsNull() } returns emptyList()
            val userList = userService.findAll()
            assertTrue(userList.isEmpty())
        }
    }

    @DisplayName(value = "Tests for function findByUsername")
    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class FindByUsername {
        @Test
        fun `successfully find user by username`() {
            every { userRepository.findByDeletedAtIsNullAndActivationIsNullAndUsername(any()) } returns Optional.of(user)
            val result = userService.findByUsername("user")
            assertEquals(user, result)
        }

        @Test
        fun `failed to find user by username`() {
            every { userRepository.findByDeletedAtIsNullAndActivationIsNullAndUsername(any()) } returns Optional.empty()
            assertThrows<DocumentNotFoundException> { userService.findByUsername("user") }
        }
    }
    @DisplayName(value = "Tests for function update")
    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class Update {
        @Test
        fun `successfully update user`() {
            every { userRepository.findByDeletedAtIsNullAndActivationIsNullAndUsername(any()) } returns Optional.of(user)
            every { userRepository.save(any()) } returns user
            userService.update(dto, "user")
            verifySequence {
                userRepository.findByDeletedAtIsNullAndActivationIsNullAndUsername(any())
                userRepository.save(any())
            }
        }

        @Test
        fun `failed update user because not found`() {
            every { userRepository.findByDeletedAtIsNullAndActivationIsNullAndUsername(any()) } returns Optional.empty()
            assertThrows<DocumentNotFoundException> { userService.update(dto, "user") }
        }
    }
}
