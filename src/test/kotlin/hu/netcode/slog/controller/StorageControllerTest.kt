package hu.netcode.slog.controller

import com.amazonaws.AmazonClientException
import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import hu.netcode.slog.configuration.ObjectMapperConfiguration
import hu.netcode.slog.data.dto.input.ObjectDto
import hu.netcode.slog.data.dto.output.ObjectMetadataDto
import hu.netcode.slog.service.ExceptionService
import hu.netcode.slog.service.StorageService
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import java.net.URI

@AutoConfigureMockMvc
@ContextConfiguration(
    classes = [
        ObjectMapperConfiguration::class
    ]
)
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
@WebMvcTest(
    value = [
        ExceptionService::class,
        StorageController::class
    ]
)
class StorageControllerTest {
    private companion object {
        const val ERROR_404 = "Status Code: 404 Error"
        const val ERROR_500 = "Status Code: 500 Error"
        const val LOCATION_URL = "https://s3/bucket/key"
        const val URL = "/api/storage"
    }

    @Autowired
    private lateinit var exceptionService: ExceptionService
    @Autowired
    private lateinit var mockMvc: MockMvc
    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockkBean(relaxed = true)
    private lateinit var storageService: StorageService

    @BeforeEach
    fun init() {
        clearAllMocks()
    }

    @DisplayName(value = "StorageController: Tests for function add")
    @Nested
    @TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
    inner class Add {
        private val dto = mockk<ObjectDto>(relaxed = true)
        private val url = "$URL/buckets/key"

        @Test
        fun `successfully add object`() {
            every {
                storageService.putObject(any(), any(), any<String>(), any())
            } returns ResponseEntity.created(URI(LOCATION_URL)).build()
            mockMvc.post(url) {
                accept = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(dto)
                contentType = MediaType.APPLICATION_JSON
            }.andExpect {
                status { isCreated }
            }
        }

        @Test
        fun `fail to add object because of a not found AmazonClientException`() {
            every {
                storageService.putObject(any(), any(), any<String>(), any())
            } throws AmazonClientException(ERROR_404)
            mockMvc.post(url) {
                accept = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(dto)
                contentType = MediaType.APPLICATION_JSON
            }.andExpect {
                status { isNotFound }
            }
        }

        @Test
        fun `fail to add object because of an internal server error AmazonClientException`() {
            every {
                storageService.putObject(any(), any(), any<String>(), any())
            } throws AmazonClientException(ERROR_500)
            mockMvc.post(url) {
                accept = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(dto)
                contentType = MediaType.APPLICATION_JSON
            }.andExpect {
                status { isInternalServerError }
            }
        }
    }

    @DisplayName(value = "StorageController: Tests for function buckets")
    @Nested
    @TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
    inner class Buckets {
        @Test
        fun `successfully list all buckets`() {
            mockMvc.get("$URL/buckets") {
                accept = MediaType.APPLICATION_JSON
                contentType = MediaType.APPLICATION_JSON
            }.andExpect {
                status { isOk }
            }
        }
    }

    @DisplayName(value = "StorageController: Tests for function delete")
    @Nested
    @TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
    inner class Delete {
        @Test
        fun `successfully delete an object`() {
            every { storageService.deleteObject(any(), any()) } returns ResponseEntity.noContent().build()
            mockMvc.delete("$URL/buckets/bucket/key") {
                accept = MediaType.APPLICATION_JSON
                contentType = MediaType.APPLICATION_JSON
            }.andExpect {
                status { isNoContent }
            }
        }

        @Test
        fun `fail to delete an object because of a not found AmazonClientException`() {
            every { storageService.deleteObject(any(), any()) } throws AmazonClientException(ERROR_404)
            mockMvc.delete("$URL/buckets/bucket/key") {
                accept = MediaType.APPLICATION_JSON
                contentType = MediaType.APPLICATION_JSON
            }.andExpect {
                status { isNotFound }
            }
        }

        @Test
        fun `fail to delete an object because of an internal server error AmazonClientException`() {
            every { storageService.deleteObject(any(), any()) } throws AmazonClientException(ERROR_500)
            mockMvc.delete("$URL/buckets/bucket/key") {
                accept = MediaType.APPLICATION_JSON
                contentType = MediaType.APPLICATION_JSON
            }.andExpect {
                status { isInternalServerError }
            }
        }
    }

    @DisplayName(value = "StorageController: Tests for function meta")
    @Nested
    @TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
    inner class Meta {
        private val dto = mockk<ObjectMetadataDto>(relaxed = true)
        private val url = "$URL/buckets/bucket/key/meta"

        @Test
        fun `successfully get an object meta from a bucket`() {
            every { storageService.getObjectMetaData(any(), any()) } returns dto
            mockMvc.get(url) {
                accept = MediaType.APPLICATION_JSON
                contentType = MediaType.APPLICATION_JSON
            }.andExpect {
                status { isOk }
            }
        }

        @Test
        fun `fail to get an object meta data because of a not found AmazonClientException`() {
            every { storageService.getObjectMetaData(any(), any()) } throws AmazonClientException(ERROR_404)
            mockMvc.get(url) {
                accept = MediaType.APPLICATION_JSON
                contentType = MediaType.APPLICATION_JSON
            }.andExpect {
                status { isNotFound }
            }
        }

        @Test
        fun `fail to get an object meta data because of an internal server error AmazonClientException`() {
            every { storageService.getObjectMetaData(any(), any()) } throws AmazonClientException(ERROR_500)
            mockMvc.get(url) {
                accept = MediaType.APPLICATION_JSON
                contentType = MediaType.APPLICATION_JSON
            }.andExpect {
                status { isInternalServerError }
            }
        }
    }

    @DisplayName(value = "StorageController: Tests for function object")
    @Nested
    @TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
    inner class Object {
        private val url = "$URL/buckets/bucket/key"

        @Test
        fun `successfully get an object from a bucket`() {
            every { storageService.getObject(any(), any()) } returns ResponseEntity.ok().build()
            mockMvc.get(url) {
                accept = MediaType.APPLICATION_JSON
                contentType = MediaType.APPLICATION_JSON
            }.andExpect {
                status { isOk }
            }
        }

        @Test
        fun `fail to get an object because of a not found AmazonClientException`() {
            every { storageService.getObject(any(), any()) } throws AmazonClientException(ERROR_404)
            mockMvc.get(url) {
                accept = MediaType.APPLICATION_JSON
                contentType = MediaType.APPLICATION_JSON
            }.andExpect {
                status { isNotFound }
            }
        }

        @Test
        fun `fail to get an object because of an internal server error AmazonClientException`() {
            every { storageService.getObject(any(), any()) } throws AmazonClientException(ERROR_500)
            mockMvc.get(url) {
                accept = MediaType.APPLICATION_JSON
                contentType = MediaType.APPLICATION_JSON
            }.andExpect {
                status { isInternalServerError }
            }
        }
    }

    @DisplayName(value = "StorageController: Tests for function objects")
    @Nested
    @TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
    inner class Objects {
        @Test
        fun `successfully list all objects from a bucket`() {
            mockMvc.get("$URL/buckets/bucket") {
                accept = MediaType.APPLICATION_JSON
                contentType = MediaType.APPLICATION_JSON
            }.andExpect {
                status { isOk }
            }
        }
    }
}
