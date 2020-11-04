package hu.netcode.slog.service

import com.amazonaws.AmazonClientException
import com.amazonaws.services.s3.model.Bucket
import com.amazonaws.services.s3.model.ObjectListing
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.S3ObjectSummary
import com.ninjasquad.springmockk.MockkBean
import hu.netcode.slog.result.Result
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
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
import org.springframework.http.HttpStatus
import java.io.IOException
import java.lang.IllegalArgumentException
import java.net.URI
import java.time.ZonedDateTime
import java.util.Base64
import java.util.Date

@SpringBootTest(
    classes = [
        StorageService::class
    ]
)
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
class StorageServiceTest {
    private companion object {
        const val BUCKET = "bucket"
        const val DATA = "data"
        const val KEY = "key"
        const val MIME_TEXT_PLAIN = "text/plain"
    }

    @Autowired
    private lateinit var storageService: StorageService

    @MockkBean(relaxed = true)
    private lateinit var s3Service: S3Service

    @BeforeEach
    fun init() {
        clearAllMocks()
    }

    @DisplayName(value = "StorageService: Tests for function delete object")
    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class DeleteObject {
        @Test
        fun `successfully delete object`() {
            every { s3Service.getObject(any(), any()) } returns Result.Success(mockk(relaxed = true))
            every { s3Service.deleteObject(any(), any()) } returns Result.Success(mockk(relaxed = true))
            val result = storageService.deleteObject(BUCKET, KEY)
            assertEquals(HttpStatus.NO_CONTENT, result.statusCode)
            verifySequence {
                s3Service.getObject(any(), any())
                s3Service.deleteObject(any(), any())
            }
        }

        @Test
        fun `fail to delete object because of an AmazonClientException during get object`() {
            every { s3Service.getObject(any(), any()) } returns Result.Failure(error = AmazonClientException(""))
            assertThrows<AmazonClientException> { storageService.deleteObject(BUCKET, KEY) }
        }

        @Test
        fun `fail to delete object because of an IOException during get object`() {
            every { s3Service.getObject(any(), any()) } returns Result.Failure(error = IOException(""))
            assertThrows<IOException> { storageService.deleteObject(BUCKET, KEY) }
        }

        @Test
        fun `fail to delete object because of an AmazonClientException during object delete`() {
            every { s3Service.getObject(any(), any()) } returns Result.Success(mockk(relaxed = true))
            every { s3Service.deleteObject(any(), any()) } returns Result.Failure(error = AmazonClientException(""))
            assertThrows<AmazonClientException> { storageService.deleteObject(BUCKET, KEY) }
        }
    }

    @DisplayName(value = "StorageService: Tests for function put object")
    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class PutObject {
        @Test
        fun `successfully put string data`() {
            val getUrlResult = mockk<Result.Success<URI>>(relaxed = true)
            every { getUrlResult.value } returns URI("http://localhost")
            every { s3Service.putObject(any(), any(), any(), any()) } returns Result.Success(mockk(relaxed = true))
            every { s3Service.getUrl(any(), any()) } returns getUrlResult
            val result = storageService.putObject(BUCKET, KEY, DATA, MIME_TEXT_PLAIN)
            assertEquals(HttpStatus.CREATED, result.statusCode)
        }

        @Test
        fun `failed to put string data because of an AmazonClientException`() {
            every { s3Service.putObject(any(), any(), any(), any()) } returns Result.Failure(AmazonClientException(""))
            assertThrows<AmazonClientException> { storageService.putObject(BUCKET, KEY, DATA, MIME_TEXT_PLAIN) }
        }

        @Test
        fun `failed to put string data because of an exception during get url`() {
            every { s3Service.putObject(any(), any(), any(), any()) } returns Result.Success(mockk(relaxed = true))
            every { s3Service.getUrl(any(), any()) } returns Result.Failure(Exception(""))
            assertThrows<Exception> { storageService.putObject(BUCKET, KEY, DATA, MIME_TEXT_PLAIN) }
        }

        @Test
        fun `successfully put string data but fail during decode`() {
            mockkStatic(Base64::class)
            every { Base64.getDecoder().decode(any<String>()) } throws IllegalArgumentException("")
            every { s3Service.putObject(any(), any(), any(), any()) } returns Result.Success(mockk(relaxed = true))
            every { s3Service.getUrl(any(), any()) } returns Result.Success(mockk(relaxed = true))
            val result = storageService.putObject(BUCKET, KEY, DATA, MIME_TEXT_PLAIN)
            assertEquals(HttpStatus.CREATED, result.statusCode)
        }
    }

    @DisplayName(value = "StorageService: Tests for function get object")
    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetObject {
        @Test
        fun `successfully get object`() {
            every { s3Service.getObject(any(), any()) } returns Result.Success(mockk(relaxed = true))
            val result = storageService.getObject(BUCKET, KEY)
            assertEquals(HttpStatus.OK, result.statusCode)
        }
        @Test
        fun `failed to get object because of an AmazonClientException`() {
            every { s3Service.getObject(any(), any()) } returns Result.Failure(AmazonClientException(""))
            assertThrows<AmazonClientException> { storageService.getObject(BUCKET, KEY) }
        }
    }
    @DisplayName(value = "StorageService: Tests for function get object metadata")
    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetObjectMetaData {
        @Test
        fun `successfully get object metadata`() {
            val objectMetaData = mockk<ObjectMetadata>(relaxed = true)
            every { s3Service.getObjectMetaData(any(), any()) } returns Result.Success(objectMetaData)
            storageService.getObjectMetaData(BUCKET, KEY)
            verifySequence {
                s3Service.getObjectMetaData(any(), any())
            }
        }

        @Test
        fun `fail to get object metadata because of an AmazonClientException`() {
            every { s3Service.getObjectMetaData(any(), any()) } returns Result.Failure(AmazonClientException(""))
            assertThrows<AmazonClientException> { storageService.getObjectMetaData(BUCKET, KEY) }
        }
    }

    @DisplayName(value = "StorageService: Tests for function list buckets")
    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class ListBuckets {
        @Test
        fun `successfully list buckets`() {
            val bucket = mockk<Bucket>(relaxed = true)
            every { bucket.name } returns BUCKET
            every { s3Service.listBuckets() } returns Result.Success(listOf(bucket))
            val result = storageService.listBuckets()
            assertTrue(result.isNotEmpty())
            assertEquals(1, result.size)
            assertEquals(BUCKET, result[0].name)
        }

        @Test
        fun `fail to list buckets because of an AmazonClientException`() {
            every { s3Service.listBuckets() } returns Result.Failure(AmazonClientException(""))
            assertThrows<AmazonClientException> { storageService.listBuckets() }
        }
    }

    @DisplayName(value = "StorageService: Tests for function list objects")
    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class ListObjects {
        @Test
        fun `successfully list objects`() {
            val objectListing = mockk<ObjectListing>(relaxed = true)
            val s3ObjectSummary = mockk<S3ObjectSummary>(relaxed = true)
            val s3ObjectSummaryList = listOf(s3ObjectSummary)
            every { s3ObjectSummary.key } returns "key"
            every { s3ObjectSummary.size } returns 1000
            every { s3ObjectSummary.lastModified } returns Date.from(ZonedDateTime.now().toInstant())
            every { objectListing.bucketName } returns BUCKET
            every { objectListing.objectSummaries } returns s3ObjectSummaryList
            every { s3Service.listObjects(any()) } returns Result.Success(objectListing)
            val s3ObjectDtoList = storageService.listObjects(BUCKET)
            assertEquals(1, s3ObjectDtoList.size)
            verifySequence {
                s3Service.listObjects(BUCKET)
            }
        }

        @Test
        fun `fail to list objects because of an AmazonClientException`() {
            every { s3Service.listObjects(any()) } returns Result.Failure(AmazonClientException(""))
            assertThrows<AmazonClientException> { storageService.listObjects(BUCKET) }
        }
    }
}
