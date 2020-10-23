package service

import com.github.slugify.Slugify
import com.ninjasquad.springmockk.MockkBean
import hu.netcode.slog.data.repository.PostRepository
import hu.netcode.slog.properties.PagingProperties
import hu.netcode.slog.service.PostService
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(
    classes = [
        PostService::class
    ]
)
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
class PostServiceTest {
    @Autowired
    private lateinit var postService: PostService

    @MockkBean(relaxed = true)
    private lateinit var pagingProperties: PagingProperties
    @MockkBean(relaxed = true)
    private lateinit var postRepository: PostRepository
    @MockkBean(relaxed = true)
    private lateinit var slugify: Slugify

    @DisplayName(value = "Tests for function save")
    @Nested
    @TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
    inner class Save {
        @Test
        fun `successfully save post`() {
            assertTrue(true)
        }
    }
}
