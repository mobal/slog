package hu.netcode.slog.service

import com.amazonaws.AmazonClientException
import hu.netcode.slog.data.dto.output.BucketDto
import hu.netcode.slog.data.dto.output.S3ObjectDto
import hu.netcode.slog.extension.toBucketDto
import hu.netcode.slog.extension.toS3ObjectDto
import hu.netcode.slog.result.Result
import java.io.ByteArrayInputStream
import java.lang.IllegalArgumentException
import java.util.Base64
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class StorageService(
    private val s3Service: S3Service
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    private fun decode(data: String): Result<ByteArray> {
        return try {
            val result = Result.Success(Base64.getDecoder().decode(data))
            logger.info("Data successfully decoded from Base64 encoded string")
            result
        } catch (ex: IllegalArgumentException) {
            logger.info("Failed to decode string because of an exception. It is possible that is not a binary file {}", ex)
            Result.Failure(ex)
        }
    }

    @Throws(exceptionClasses = [AmazonClientException::class])
    fun put(bucketName: String, key: String, data: String, mime: String): ResponseEntity<Unit> {
        return when (val result = decode(data)) {
            is Result.Success -> {
                put(bucketName, key, result.value, mime)
            }
            is Result.Failure -> {
                put(bucketName, key, data.toByteArray(), mime)
            }
        }
    }

    @Throws(exceptionClasses = [AmazonClientException::class])
    fun put(bucketName: String, key: String, bytes: ByteArray, mime: String): ResponseEntity<Unit> {
        when (val putObjectResult = s3Service.putObject(bucketName, key, ByteArrayInputStream(bytes), mime)) {
            is Result.Success -> {
                logger.info("""Object "{}" stored successfully""".trimMargin(), key)
                when (val getUrlResult = s3Service.getUrl(bucketName, key)) {
                    is Result.Success -> {
                        return ResponseEntity.created(getUrlResult.value)
                                .build()
                    }
                    is Result.Failure -> {
                        throw getUrlResult.error
                    }
                }
            }
            is Result.Failure -> {
                throw putObjectResult.error
            }
        }
    }

    @Throws(exceptionClasses = [AmazonClientException::class])
    fun getObject(bucketName: String, key: String): ByteArray {
        when (val result = s3Service.getObject(bucketName, key)) {
            is Result.Success -> {
                return byteArrayOf()
            }
            is Result.Failure -> {
                throw result.error
            }
        }
    }

    fun listBuckets(): List<BucketDto> {
        when (val result = s3Service.listBuckets()) {
            is Result.Success -> {
                return result.value.map { it.toBucketDto() }
            }
            is Result.Failure -> {
                throw result.error
            }
        }
    }

    fun listObjects(bucketName: String): List<S3ObjectDto> {
        when (val result = s3Service.listObjects(bucketName)) {
            is Result.Success -> {
                return result.value.objectSummaries.map { it.toS3ObjectDto() }
            }
            is Result.Failure -> {
                throw result.error
            }
        }
    }
}
