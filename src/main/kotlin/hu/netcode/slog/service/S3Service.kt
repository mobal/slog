package hu.netcode.slog.service

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

    fun listBuckets() {
        //
    }

    fun listObjects() {
        //
    }

    fun putObject() {
        //
    }
}
