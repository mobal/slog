package hu.netcode.slog.service

import com.github.slugify.Slugify
import com.ninjasquad.springmockk.MockkBean
import hu.netcode.slog.data.document.Meta
import hu.netcode.slog.data.document.Post
import hu.netcode.slog.data.dto.input.PostDto
import hu.netcode.slog.data.repository.PostRepository
import hu.netcode.slog.exception.DocumentNotFoundException
import hu.netcode.slog.properties.PagingProperties
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.just
import io.mockk.verifySequence
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.ZonedDateTime
import java.util.Optional

@SpringBootTest(
    classes = [
        PostService::class
    ]
)
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
class PostServiceTest {
    private companion object {
        const val ERROR_MESSAGE_POST_NOT_FOUND = "The requested post was not found"
        const val PAGE_SIZE = 10
        const val SLUG = "Slug"

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

    @BeforeEach
    fun init() {
        clearAllMocks()
        every { pagingProperties.size } returns PAGE_SIZE
    }

    @Autowired
    private lateinit var postService: PostService

    @MockkBean
    private lateinit var pagingProperties: PagingProperties
    @MockkBean(relaxed = true)
    private lateinit var postRepository: PostRepository
    @MockkBean(relaxed = true)
    private lateinit var slugify: Slugify

    @DisplayName(value = "PostService: Tests for function delete")
    @Nested
    @TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
    inner class Delete {
        @Test
        fun `successfully delete post`() {
            every { postRepository.deleteByMetaSlug(any()) } just Runs
            postService.delete("slug")
            verifySequence {
                postRepository.deleteByMetaSlug(any())
            }
        }

        fun `fail to delete post`() {
            // TODO
        }
    }

    @DisplayName(value = "PostService: Tests for function findAll")
    @Nested
    @TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
    inner class FindAll {
        @Test
        fun `successfully find all posts`() {
            every {
                postRepository.findByVisibleTrueAndDeletedAtIsNullAndPublishedAtBefore(any(), any())
            } returns listOf(post)
            val postList = postService.findAll(1)
            assertFalse(postList.isEmpty())
            assertEquals(1, postList.size)
            assertEquals(post, postList[0])
        }

        @Test
        fun `successfully find all posts but return is empty`() {
            every {
                postRepository.findByVisibleTrueAndDeletedAtIsNullAndPublishedAtBefore(any(), any())
            } returns emptyList()
            val resultList = postService.findAll(1)
            assertTrue(resultList.isEmpty())
        }
    }

    @DisplayName(value = "PostService: Tests for function findBySlug")
    @Nested
    @TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
    inner class FindBySlug {
        @Test
        fun `successfully find post by slug`() {
            every {
                postRepository.findByVisibleTrueAndDeletedAtIsNullAndPublishedAtBeforeAndMetaSlug(any(), any())
            } returns Optional.of(post)
            val result = postService.findBySlug(SLUG)
            assertEquals(post, result)
        }

        @Test
        fun `failed to find post by slug`() {
            val slug = "test"
            every {
                postRepository.findByVisibleTrueAndDeletedAtIsNullAndPublishedAtBeforeAndMetaSlug(any(), any())
            } returns Optional.empty()
            val ex = assertThrows<DocumentNotFoundException> {
                postService.findBySlug(slug)
            }
            assertEquals("$ERROR_MESSAGE_POST_NOT_FOUND: $slug", ex.message)
        }
    }

    @DisplayName(value = "PostService: Tests for function save")
    @Nested
    @TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
    inner class Save {
        @Test
        fun `successfully save post`() {
            every { postRepository.save(any()) } returns post
            val result = postService.save(dto)
            assertEquals(post, result)
        }

        fun `fail to save post`() {
            // TODO
        }
    }

    @DisplayName(value = "PostService: Tests for function update")
    @Nested
    @TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
    inner class Update {
        @Test
        fun `successfully update post`() {
            every {
                postRepository.findByVisibleTrueAndDeletedAtIsNullAndPublishedAtBeforeAndMetaSlug(any(), any())
            } returns Optional.of(post)
            every { postRepository.save(any()) } returns post
            postService.update(dto, SLUG)
            verifySequence {
                postRepository.findByVisibleTrueAndDeletedAtIsNullAndPublishedAtBeforeAndMetaSlug(any(), any())
                postRepository.save(any())
            }
        }

        @Test
        fun `failed to update post`() {
            every {
                postRepository.findByVisibleTrueAndDeletedAtIsNullAndPublishedAtBeforeAndMetaSlug(any(), any())
            } returns Optional.empty()
            val ex = assertThrows<DocumentNotFoundException> {
                postService.update(dto, SLUG)
            }
            assertEquals("$ERROR_MESSAGE_POST_NOT_FOUND: $SLUG", ex.message)
        }
    }
}
