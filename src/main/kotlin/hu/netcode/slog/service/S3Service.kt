package hu.netcode.slog.service

import com.amazonaws.AmazonClientException
import com.amazonaws.services.s3.AmazonS3
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class S3Service(
    private val s3Client: AmazonS3
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    fun copyObject() {
        //
    }

    fun createBucket() {
        //
    }

    fun deleteBucket() {
        //
    }

    fun deleteObject() {
        //
    }

    fun deleteObjects() {
        //
    }

    fun getObject() {
        //
    }

    fun listBuckets(): List<String> {
        try {
            return s3Client.listBuckets()
                .map { it.name }
                .toList()
        } catch (ex: AmazonClientException) {
            logger.error("Failed to list buckets because of an exception: {}", ex)
            throw ex
        }
    }

    fun listObjects(bucket: String): List<String> {
        try {
            return s3Client.listObjects(bucket)
                .objectSummaries
                .map { it.key }
                .toList()
        } catch (ex: AmazonClientException) {
            logger.error("Failed to list object from bucket {} because of an exception: {}", bucket, ex)
            throw ex
        }
    }

    fun putObject() {
        //
    }
}
