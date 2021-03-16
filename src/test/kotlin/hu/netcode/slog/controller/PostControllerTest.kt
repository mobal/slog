package hu.netcode.slog.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import hu.netcode.slog.data.document.Meta
import hu.netcode.slog.data.document.Post
import hu.netcode.slog.data.dto.input.PostDto
import hu.netcode.slog.exception.DocumentNotFoundException
import hu.netcode.slog.service.ExceptionService
import hu.netcode.slog.service.PostService
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
import java.time.ZonedDateTime

@AutoConfigureMockMvc
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
@WebMvcTest(
    value = [
        ExceptionService::class,
        PostController::class
    ]
)
class PostControllerTest {
    private companion object {
        const val SLUG = "Slug"
        const val URL = "/api/posts"

        val dto = PostDto(
            author = "Author",
            body = "Body",
            title = "Title",
            tagList = listOf("tag")
        )
        val post = Post(
            id = "5f93308afc9f5f27f6724ac3",
            author = "Author",
            body = "Body",
            meta = Meta(
                readingTime = 5,
                slug = SLUG
            ),
            title = "Title",
            tagList = listOf("tag"),
            visible = true,
            createdAt = ZonedDateTime.now(),
            publishedAt = ZonedDateTime.now(),
            updatedAt = ZonedDateTime.now()
        )
    }

    @Autowired
    private lateinit var exceptionService: ExceptionService
    @Autowired
    private lateinit var mockMvc: MockMvc
    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockkBean(relaxed = true)
    private lateinit var postService: PostService

    @BeforeEach
    fun init() {
        clearAllMocks()
    }

    @DisplayName(value = "PostController: Tests for function create")
    @Nested
    @TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
    inner class Create {
        @Test
        fun `successfully create post`() {
            mockMvc.post(URL) {
                content = objectMapper.writeValueAsString(dto)
                contentType = MediaType.APPLICATION_JSON
                with(csrf())
                with(oauth2Login())
            }.andExpect {
                status { isCreated() }
            }
        }

        @Test
        fun `failed to create post because validation fails`() {
            mockMvc.post(URL) {
                accept = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(
                    PostDto(author = "", body = "", title = "", tagList = emptyList())
                )
                contentType = MediaType.APPLICATION_JSON
                with(csrf())
                with(oauth2Login())
            }.andExpect {
                status { isBadRequest() }
            }
        }

        @Test
        fun `failed to create post without login`() {
            mockMvc.post(URL) {
                accept = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(dto)
                contentType = MediaType.APPLICATION_JSON
                with(csrf())
            }.andExpect {
                status { isForbidden() }
            }
        }
    }

    @DisplayName(value = "PostController: Tests for function delete")
    @Nested
    @TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
    inner class Delete {
        private val url = "$URL/$SLUG"
        @Test
        fun `successfully delete post`() {
            every { postService.delete(any()) } just Runs
            mockMvc.delete(url) {
                accept = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(dto)
                contentType = MediaType.APPLICATION_JSON
                with(csrf())
                with(oauth2Login())
            }.andExpect {
                status { isNoContent() }
            }
            verifySequence { postService.delete(any()) }
        }

        @Test
        fun `failed to delete post`() {
            every { postService.delete(any()) } throws Exception("")
            mockMvc.delete(url) {
                accept = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(dto)
                contentType = MediaType.APPLICATION_JSON
                with(csrf())
                with(oauth2Login())
            }.andExpect {
                status { isInternalServerError() }
            }
            verifySequence { postService.delete(any()) }
        }

        @Test
        fun `failed to delete post without login`() {
            mockMvc.delete(url) {
                accept = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(dto)
                contentType = MediaType.APPLICATION_JSON
                with(csrf())
            }.andExpect {
                status { isForbidden() }
            }
        }
    }

    @DisplayName(value = "PostController: Tests for function findAll")
    @Nested
    @TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
    inner class FindAll {
        @Test
        fun `successfully find all posts`() {
            every { postService.findAll(any()) } returns listOf(post)
            mockMvc.get(URL) {
                accept = MediaType.APPLICATION_JSON
                contentType = MediaType.APPLICATION_JSON
            }.andExpect {
                content { json(objectMapper.writeValueAsString(listOf(post))) }
                status { isOk() }
            }
        }

        @Test
        fun `successfully find all posts but result is empty`() {
            every { postService.findAll(any()) } returns emptyList()
            mockMvc.get(URL) {
                accept = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(dto)
                contentType = MediaType.APPLICATION_JSON
            }.andExpect {
                content { json("[]") }
                status { isOk() }
            }
        }
    }

    @DisplayName(value = "PostController: Tests for function findBySlug")
    @Nested
    @TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
    inner class FindBySlug {
        private val url = "$URL/$SLUG"
        @Test
        fun `successfully find post by slug`() {
            every { postService.findBySlug(any()) } returns post
            mockMvc.get(url) {
                accept = MediaType.APPLICATION_JSON
                contentType = MediaType.APPLICATION_JSON
            }.andExpect {
                content { json(objectMapper.writeValueAsString(post)) }
                status { isOk() }
            }
        }

        @Test
        fun `failed to find post by slug`() {
            every { postService.findBySlug(any()) } throws DocumentNotFoundException("")
            mockMvc.get(url) {
                accept = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(dto)
                contentType = MediaType.APPLICATION_JSON
            }.andExpect {
                status { isNotFound() }
            }
        }
    }

    @DisplayName(value = "PostController: Tests for function update")
    @Nested
    @TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
    inner class Update {
        private val url = "$URL/$SLUG"
        @Test
        fun `successfully update post`() {
            every { postService.update(any(), any()) } just Runs
            mockMvc.put(url) {
                accept = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(dto)
                contentType = MediaType.APPLICATION_JSON
                with(csrf())
                with(oauth2Login())
            }.andExpect {
                status { isOk() }
            }
            verifySequence { postService.update(any(), any()) }
        }

        @Test
        fun `failed to update post`() {
            every { postService.update(any(), any()) } throws Exception("")
            mockMvc.put(url) {
                accept = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(dto)
                contentType = MediaType.APPLICATION_JSON
                with(csrf())
                with(oauth2Login())
            }.andExpect {
                status { isInternalServerError() }
            }
            verifySequence { postService.update(any(), any()) }
        }

        @Test
        fun `failed to update post because csrf token is invalid`() {
            mockMvc.put(url) {
                accept = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(dto)
                contentType = MediaType.APPLICATION_JSON
                with(csrf().useInvalidToken())
            }.andExpect {
                status { isForbidden() }
            }
        }

        @Test
        fun `failed to update post without login`() {
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
