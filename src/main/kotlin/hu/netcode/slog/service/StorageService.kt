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
import org.springframework.http.CacheControl
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
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
            logger.info("Failed to decode string because of an exception. It is possible that is not a binary file {}",
                    ex)
            Result.Failure(ex)
        }
    }

    fun deleteObject(bucketName: String, key: String): ResponseEntity<Unit> {
        when (val getObjectResult = s3Service.getObject(bucketName, key)) {
            is Result.Success -> {
                when (val deleteObjectResult = s3Service.deleteObject(bucketName, key)) {
                    is Result.Success -> {
                        return ResponseEntity.noContent()
                                .build<Unit>()
                    }
                    is Result.Failure -> {
                        throw deleteObjectResult.error
                    }
                }
            }
            is Result.Failure -> {
                throw getObjectResult.error
            }
        }
    }

    @Throws(exceptionClasses = [AmazonClientException::class])
    fun putObject(bucketName: String, key: String, data: String, mime: String): ResponseEntity<Unit> {
        return when (val result = decode(data)) {
            is Result.Success -> {
                putObject(bucketName, key, result.value, mime)
            }
            is Result.Failure -> {
                putObject(bucketName, key, data.toByteArray(), mime)
            }
        }
    }

    @Throws(exceptionClasses = [AmazonClientException::class])
    fun putObject(bucketName: String, key: String, bytes: ByteArray, mime: String): ResponseEntity<Unit> {
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
    fun getObject(bucketName: String, key: String): ResponseEntity<ByteArray> {
        when (val result = s3Service.getObject(bucketName, key)) {
            is Result.Success -> {
                return ResponseEntity(
                    result.value,
                    HttpHeaders().apply {
                        setCacheControl(CacheControl.noCache())
                    },
                    HttpStatus.OK
                )
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
