package hu.netcode.slog.service

import com.amazonaws.AmazonClientException
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.Bucket
import com.amazonaws.services.s3.model.CopyObjectResult
import com.amazonaws.services.s3.model.DeleteObjectsRequest
import com.amazonaws.services.s3.model.DeleteObjectsResult
import com.amazonaws.services.s3.model.ObjectListing
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectResult
import java.io.IOException
import java.io.InputStream
import org.modelmapper.ModelMapper
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class S3Service(
    private val modelMapper: ModelMapper,
    private val s3Client: AmazonS3
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    fun copyObject(
        sourceBucketName: String,
        sourceKey: String,
        destinationBucketName: String,
        destinationKey: String
    ): CopyObjectResult {
        try {
            return s3Client.copyObject(sourceBucketName, sourceKey, destinationBucketName, destinationKey)
        } catch (ex: AmazonClientException) {
            logger.error("Failed to copy object {} as {} from bucket {} to {}", sourceKey, destinationKey,
                sourceBucketName, destinationBucketName)
            throw ex
        }
    }

    fun createBucket(bucketName: String): Bucket {
        try {
            return s3Client.createBucket(bucketName)
        } catch (ex: AmazonClientException) {
            logger.error("Failed to create bucket because of an exception: {}", ex)
            throw ex
        }
    }

    fun deleteBucket(bucketName: String) {
        try {
            s3Client.deleteBucket(bucketName)
        } catch (ex: AmazonClientException) {
            logger.error("Failed to delete bucket {} because of an exception: {}", bucketName, ex)
            throw ex
        }
    }

    fun deleteObject(bucketName: String, key: String) {
        try {
            s3Client.deleteObject(bucketName, key)
        } catch (ex: AmazonClientException) {
            logger.error("Failed to delete object {} because of an exception: {}", key, ex)
            throw ex
        }
    }

    fun deleteObjects(bucketName: String, objectKeyList: List<String>): DeleteObjectsResult {
        try {
            val req = DeleteObjectsRequest(bucketName)
            req.keys = objectKeyList.map { DeleteObjectsRequest.KeyVersion(it) }.toList()
            return s3Client.deleteObjects(req)
        } catch (ex: AmazonClientException) {
            logger.error("Failed to delete objects from bucket {} because of an exception: {}", bucketName, ex)
            throw ex
        }
    }

    fun getObject(bucketName: String, key: String): ByteArray {
        try {
            return s3Client.getObject(bucketName, key)
                .objectContent
                .readAllBytes()
        } catch (ex: AmazonClientException) {
            logger.error("Failed to get object {} from bucket {} because of an exception: {}", key, bucketName, ex)
            throw ex
        } catch (ex: IOException) {
            logger.error("Failed to convert object {} to byte array because of an exception: {}", key, ex)
            throw ex
        }
    }

    fun listBuckets(): List<Bucket> {
        try {
            return s3Client.listBuckets()
        } catch (ex: AmazonClientException) {
            logger.error("Failed to list buckets because of an exception: {}", ex)
            throw ex
        }
    }

    fun listObjects(bucketName: String): ObjectListing {
        try {
            return s3Client.listObjects(bucketName)
        } catch (ex: AmazonClientException) {
            logger.error("Failed to list object from bucket {} because of an exception: {}", bucketName, ex)
            throw ex
        }
    }

    fun putObject(bucketName: String, key: String, data: InputStream, mime: String): PutObjectResult {
        try {
            val metaData = ObjectMetadata()
            metaData.contentType = "meta"
            return s3Client.putObject(bucketName, key, data, metaData)
        } catch (ex: AmazonClientException) {
            logger.error("Failed to put object because of an exception: {}", ex)
            throw ex
        }
    }
}
