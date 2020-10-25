package hu.netcode.slog.service

import com.amazonaws.AmazonClientException
import com.amazonaws.services.s3.AmazonS3Client
import com.mongodb.internal.connection.tlschannel.util.Util.assertTrue
import com.ninjasquad.springmockk.MockkBean
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifySequence
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.io.IOException
import java.io.InputStream

@SpringBootTest(
    classes = [
        S3Service::class
    ]
)
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
class S3ServiceTest {
    private companion object {
        const val BUCKET = "Bucket"
        const val KEY = "Key"
        const val MIME_TEXT_PLAIN = "text/plain"

        val isMock = mockk<InputStream>(relaxed = true)
    }

    @Autowired
    private lateinit var s3Service: S3Service

    @MockkBean(relaxed = true)
    private lateinit var s3Client: AmazonS3Client

    @BeforeEach
    fun init() {
        clearAllMocks()
    }

    @DisplayName(value = "S3Service: Tests for function copy object")
    @Nested
    @TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
    inner class CopyObject {
        @Test
        fun `successfully copy object`() {
            every { s3Client.copyObject(any(), any(), any(), any()) } returns mockk(relaxed = true)
            val result = s3Service.copyObject(BUCKET, KEY, "destinationBucket", "destinationKey")
            assertTrue(result.isSuccessful)
            verifySequence { s3Client.copyObject(any(), any(), any(), any()) }
        }

        @Test
        fun `fail to copy object because of an AmazonClientException`() {
            every { s3Client.copyObject(any(), any(), any(), any()) } throws AmazonClientException("")
            val result = s3Service.copyObject(BUCKET, KEY, "destinationBucket", "destinationKey")
            assertTrue(result.isFailure)
        }
    }

    @DisplayName(value = "S3Service: Tests for function create bucket")
    @Nested
    @TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
    inner class CreateBucket {
        @Test
        fun `successfully create bucket`() {
            every { s3Client.createBucket(any<String>()) } returns mockk(relaxed = true)
            val result = s3Service.createBucket(BUCKET)
            assertTrue(result.isSuccessful)
            verifySequence { s3Client.createBucket(any<String>()) }
        }

        @Test
        fun `fail to create bucket because of an AmazonClientException`() {
            every { s3Client.createBucket(any<String>()) } throws AmazonClientException("")
            val result = s3Service.createBucket(BUCKET)
            assertTrue(result.isFailure)
        }
    }

    @DisplayName(value = "S3Service: Tests for function delete bucket")
    @Nested
    @TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
    inner class DeleteBucket {
        @Test
        fun `successfully delete bucket`() {
            every { s3Client.deleteBucket(any<String>()) } returns mockk(relaxed = true)
            val result = s3Service.deleteBucket(BUCKET)
            assertTrue(result.isSuccessful)
            verifySequence { s3Client.deleteBucket(any<String>()) }
        }

        @Test
        fun `fail to delete bucket because of an AmazonClientException`() {
            every { s3Client.deleteBucket(any<String>()) } throws AmazonClientException("")
            val result = s3Service.deleteBucket(BUCKET)
            assertTrue(result.isFailure)
        }
    }

    @DisplayName(value = "S3Service: Tests for function delete object")
    @Nested
    @TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
    inner class DeleteObject {
        @Test
        fun `successfully delete object`() {
            every { s3Client.deleteObject(any(), any()) } returns mockk(relaxed = true)
            val result = s3Service.deleteObject(BUCKET, KEY)
            assertTrue(result.isSuccessful)
            verifySequence { s3Client.deleteObject(any(), any()) }
        }

        @Test
        fun `fail to delete object because of an AmazonClientException`() {
            every { s3Client.deleteObject(any(), any()) } throws AmazonClientException("")
            val result = s3Service.deleteObject(BUCKET, KEY)
            assertTrue(result.isFailure)
        }
    }
    @DisplayName(value = "S3Service: Tests for function delete objects")
    @Nested
    @TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
    inner class DeleteObjects {
        @Test
        fun `successfully delete objects`() {
            every { s3Client.deleteObjects(any()) } returns mockk(relaxed = true)
            val result = s3Service.deleteObjects(BUCKET, listOf(KEY))
            assertTrue(result.isSuccessful)
            verifySequence { s3Client.deleteObjects(any()) }
        }

        @Test
        fun `fail to delete objects because of an AmazonClientException`() {
            every { s3Client.deleteObjects(any()) } throws AmazonClientException("")
            val result = s3Service.deleteObjects(BUCKET, listOf(KEY))
            assertTrue(result.isFailure)
        }
    }

    @DisplayName(value = "S3Service: Tests for function get object")
    @Nested
    @TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
    inner class GetObject {
        @Test
        fun `successfully get object`() {
            every { s3Client.getObject(any<String>(), any()) } returns mockk(relaxed = true)
            val result = s3Service.getObject(BUCKET, KEY)
            assertTrue(result.isSuccessful)
            verifySequence { s3Client.getObject(any<String>(), any()) }
        }

        @Test
        fun `fail to get object because of an AmazonClientException`() {
            every { s3Client.getObject(any<String>(), any()) } throws AmazonClientException("")
            val result = s3Service.getObject(BUCKET, KEY)
            assertTrue(result.isFailure)
        }

        @Test
        fun `fail to get object because of an IOException`() {
            every { s3Client.getObject(any<String>(), any()) } throws IOException("")
            val result = s3Service.getObject(BUCKET, KEY)
            assertTrue(result.isFailure)
        }
    }

    @DisplayName(value = "S3Service: Tests for function get object metadata")
    @Nested
    @TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
    inner class GetObjectMetaData {
        @Test
        fun `successfully get object metadata`() {
            every { s3Client.getObjectMetadata(any(), any()) } returns mockk(relaxed = true)
            val result = s3Service.getObjectMetaData(BUCKET, KEY)
            assertTrue(result.isSuccessful)
            verifySequence { s3Client.getObjectMetadata(any(), any()) }
        }

        @Test
        fun `fail to get object metadata because of an AmazonClientException`() {
            every { s3Client.getObjectMetadata(any(), any()) } throws AmazonClientException("")
            val result = s3Service.getObjectMetaData(BUCKET, KEY)
            assertTrue(result.isFailure)
        }
    }

    @DisplayName(value = "S3Service: Tests for function get url")
    @Nested
    @TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
    inner class GetUrl {
        @Test
        fun `successfully get url`() {
            every { s3Client.getUrl(any(), any()) } returns mockk(relaxed = true)
            val result = s3Service.getUrl(BUCKET, KEY)
            assertTrue(result.isSuccessful)
            verifySequence { s3Client.getUrl(any(), any()) }
        }

        @Test
        fun `fail to get url because of an exception`() {
            every { s3Client.getUrl(any(), any()) } throws Exception("")
            val result = s3Service.getUrl(BUCKET, KEY)
            assertTrue(result.isFailure)
        }
    }

    @DisplayName(value = "S3Service: Tests for function list buckets")
    @Nested
    @TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
    inner class ListBuckets {
        @Test
        fun `successfully list buckets`() {
            every { s3Client.listBuckets() } returns mockk(relaxed = true)
            val result = s3Service.listBuckets()
            assertTrue(result.isSuccessful)
            verifySequence { s3Client.listBuckets() }
        }

        @Test
        fun `fail to list buckets because of an exception`() {
            every { s3Client.listBuckets() } throws AmazonClientException("")
            val result = s3Service.listBuckets()
            assertTrue(result.isFailure)
        }
    }

    @DisplayName(value = "S3Service: Tests for function list objects")
    @Nested
    @TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
    inner class ListObjects {
        @Test
        fun `successfully list objects`() {
            every { s3Client.listObjects(any<String>()) } returns mockk(relaxed = true)
            val result = s3Service.listObjects(BUCKET)
            assertTrue(result.isSuccessful)
            verifySequence { s3Client.listObjects(any<String>()) }
        }

        @Test
        fun `fail to list objects because of an exception`() {
            every { s3Client.listObjects(any<String>()) } throws AmazonClientException("")
            val result = s3Service.listObjects(BUCKET)
            assertTrue(result.isFailure)
        }
    }

    @DisplayName(value = "S3Service: Tests for function put object")
    @Nested
    @TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
    inner class PutObject {
        @Test
        fun `successfully put object`() {
            every { s3Client.putObject(any(), any(), any(), any()) } returns mockk(relaxed = true)
            val result = s3Service.putObject(BUCKET, KEY, isMock, MIME_TEXT_PLAIN)
            assertTrue(result.isSuccessful)
            verifySequence { s3Client.putObject(any(), any(), any(), any()) }
        }

        @Test
        fun `fail to put object because of an exception`() {
            every { s3Client.putObject(any(), any(), any(), any()) } throws AmazonClientException("")
            val result = s3Service.putObject(BUCKET, KEY, isMock, MIME_TEXT_PLAIN)
            assertTrue(result.isFailure)
        }
    }
}
