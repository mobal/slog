package hu.netcode.slog.service

import com.mongodb.internal.connection.tlschannel.util.Util.assertTrue
import org.apache.commons.lang3.exception.ExceptionUtils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus

@SpringBootTest(
    classes = [
        ExceptionService::class
    ]
)
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
class ExceptionServiceTest {
    private companion object {
        const val MESSAGE = "Message"
    }

    @Autowired
    private lateinit var exceptionService: ExceptionService

    @DisplayName(value = "Tests for create response map function")
    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class CreateResponseMap {
        @Test
        fun `successfully create response map`() {
            val ex = Exception(MESSAGE)
            val result = exceptionService.createResponseMap(ex, HttpStatus.NOT_FOUND)
            assertTrue(result.isNotEmpty())
            assertEquals(HttpStatus.NOT_FOUND.value(), result["code"])
            assertEquals(ExceptionUtils.getMessage(ex), result["msg"])
        }
    }
}
