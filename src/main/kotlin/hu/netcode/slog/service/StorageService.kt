package hu.netcode.slog.service

import hu.netcode.slog.result.Result
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class StorageService(
    private val s3Service: S3Service
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    fun getObject(bucketName: String, key: String): ByteArray {
        return byteArrayOf()
    }

    fun listBuckets(): List<String> {
        when (val result = s3Service.listBuckets()) {
            is Result.Success -> {
                return result.value.map { it.name }
            }
            is Result.Failure -> {
                throw result.error
            }
        }
    }

    fun listObjects(bucketName: String): List<String> {
        return emptyList()
    }
}
