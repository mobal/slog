package hu.netcode.slog.controller

import hu.netcode.slog.data.dto.output.S3ObjectDto
import hu.netcode.slog.properties.PagingProperties
import hu.netcode.slog.service.S3Service
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(
    produces = [MediaType.APPLICATION_JSON_VALUE],
    value = ["/api/s3"]
)
@Validated
class S3Controller(
    private val pagingProperties: PagingProperties,
    private val s3Service: S3Service
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @GetMapping(value = ["/buckets"])
    @ResponseStatus(value = HttpStatus.OK)
    fun buckets(): List<String> {
        // TODO: Remove business logic
        return s3Service.listBuckets()
            .map { it.name }
    }

    @GetMapping(value = ["/buckets/{bucketName}"])
    fun objects(@PathVariable bucketName: String): List<String> {
        // TODO: Remove business logic
        return s3Service.listObjects(bucketName)
            .objectSummaries
            .map { it.key }
    }

    @GetMapping(value = ["/buckets/{bucketName}/{key}"])
    @ResponseStatus(value = HttpStatus.OK)
    fun `object`(@PathVariable bucketName: String, @PathVariable key: String): ByteArray {
        return s3Service.getObject(bucketName, key)
    }
}
