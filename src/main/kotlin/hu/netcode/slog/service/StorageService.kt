package hu.netcode.slog.service

import com.amazonaws.AmazonClientException
import hu.netcode.slog.data.dto.output.BucketDto
import hu.netcode.slog.data.dto.output.S3ObjectDto
import hu.netcode.slog.extension.toBucketDto
import hu.netcode.slog.extension.toS3ObjectDto
import hu.netcode.slog.result.Result
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class StorageService(
    private val s3Service: S3Service
) {
    private val logger = LoggerFactory.getLogger(javaClass)

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
