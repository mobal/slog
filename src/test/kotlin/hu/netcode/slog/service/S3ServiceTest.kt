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

    inner class CopyObject {
        //
    }

    inner class CreateBucket {
        //
    }

    inner class DeleteBucket {
        //
    }

    inner class DeleteObject {
        //
    }
    inner class DeleteObjects {
        //
    }

    inner class GetObject {
        //
    }

    inner class GetObjectMetaData {
        //
    }

    inner class GetUrl {
        //
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
