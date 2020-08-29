package hu.netcode.slog.service

import com.amazonaws.AmazonClientException
import hu.netcode.slog.data.dto.output.BucketDto
import hu.netcode.slog.data.dto.output.S3ObjectDto
import hu.netcode.slog.extension.toBucketDto
import hu.netcode.slog.extension.toS3ObjectDto
import hu.netcode.slog.result.Result
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.io.InputStream

@Service
class StorageService(
    private val s3Service: S3Service
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Throws(exceptionClasses = [AmazonClientException::class])
    fun put(bucketName: String, key: String, data: InputStream, mime: String): ResponseEntity<Unit> {
        when (val putObjectResult = s3Service.putObject(bucketName, key, data, mime)) {
            is Result.Success -> {
                logger.info("Object {} stored successfully", key)
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
