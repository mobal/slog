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
import com.amazonaws.services.s3.model.S3Object
import hu.netcode.slog.result.Result
import java.io.IOException
import java.io.InputStream
import java.net.URI
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class S3Service(
    private val s3Client: AmazonS3
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    fun copyObject(
        sourceBucketName: String,
        sourceKey: String,
        destinationBucketName: String,
        destinationKey: String
    ): Result<CopyObjectResult> {
        return try {
            Result.Success(s3Client.copyObject(sourceBucketName, sourceKey, destinationBucketName, destinationKey))
        } catch (ex: AmazonClientException) {
            logger.error("Failed to copy object {} as {} from bucket {} to {}", sourceKey, destinationKey,
                sourceBucketName, destinationBucketName)
            Result.Failure(ex)
        }
    }

    fun createBucket(bucketName: String): Result<Bucket> {
        return try {
            Result.Success(s3Client.createBucket(bucketName))
        } catch (ex: AmazonClientException) {
            logger.error("Failed to create bucket because of an exception: {}", ex)
            Result.Failure(ex)
        }
    }

    fun deleteBucket(bucketName: String): Result<Unit> {
        return try {
            Result.Success(s3Client.deleteBucket(bucketName))
        } catch (ex: AmazonClientException) {
            logger.error("Failed to delete bucket {} because of an exception: {}", bucketName, ex)
            Result.Failure(ex)
        }
    }

    fun deleteObject(bucketName: String, key: String): Result<Unit> {
        return try {
            Result.Success(s3Client.deleteObject(bucketName, key))
        } catch (ex: AmazonClientException) {
            logger.error("Failed to delete object {} because of an exception: {}", key, ex)
            Result.Failure(ex)
        }
    }

    fun deleteObjects(bucketName: String, objectKeyList: List<String>): Result<DeleteObjectsResult> {
        return try {
            val req = DeleteObjectsRequest(bucketName)
            req.keys = objectKeyList.map { DeleteObjectsRequest.KeyVersion(it) }.toList()
            Result.Success(s3Client.deleteObjects(req))
        } catch (ex: AmazonClientException) {
            logger.error("Failed to delete objects from bucket {} because of an exception: {}", bucketName, ex)
            Result.Failure(ex)
        }
    }

    fun getObject(bucketName: String, key: String): Result<S3Object> {
        return try {
            Result.Success(s3Client.getObject(bucketName, key))
        } catch (ex: AmazonClientException) {
            logger.error("Failed to get object {} from bucket {} because of an exception: {}", key, bucketName, ex)
            Result.Failure(ex)
        } catch (ex: IOException) {
            logger.error("Failed to convert object {} to byte array because of an exception: {}", key, ex)
            Result.Failure(ex)
        }
    }

    fun getUrl(bucketName: String, key: String): Result<URI> {
        return try {
            Result.Success(s3Client.getUrl(bucketName, key).toURI())
        } catch (ex: Exception) {
            logger.error("Failed to get url of an object because on an exception: {}", ex)
            Result.Failure(ex)
        }
    }

    fun listBuckets(): Result<List<Bucket>> {
        return try {
            Result.Success(s3Client.listBuckets())
        } catch (ex: AmazonClientException) {
            logger.error("Failed to list buckets because of an exception: {}", ex)
            Result.Failure(ex)
        }
    }

    fun listObjects(bucketName: String): Result<ObjectListing> {
        return try {
            Result.Success(s3Client.listObjects(bucketName))
        } catch (ex: AmazonClientException) {
            logger.error("Failed to list object from bucket {} because of an exception: {}", bucketName, ex)
            Result.Failure(ex)
        }
    }

    fun putObject(bucketName: String, key: String, data: InputStream, mime: String): Result<PutObjectResult> {
        return try {
            val metaData = ObjectMetadata()
            metaData.contentType = "meta"
            Result.Success(s3Client.putObject(bucketName, key, data, metaData))
        } catch (ex: AmazonClientException) {
            logger.error("Failed to put object because of an exception: {}", ex)
            Result.Failure(ex)
        }
    }
}
