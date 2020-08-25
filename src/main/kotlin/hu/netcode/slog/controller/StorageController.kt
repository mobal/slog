package hu.netcode.slog.controller

import hu.netcode.slog.service.StorageService
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
class StorageController(
    private val storageService: StorageService
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @GetMapping(value = ["/buckets"])
    @ResponseStatus(value = HttpStatus.OK)
    fun buckets(): List<String> {
        return storageService.listBuckets()
    }

    @GetMapping(value = ["/buckets/{bucketName}"])
    @ResponseStatus(value = HttpStatus.OK)
    fun objects(@PathVariable bucketName: String): List<String> {
        return storageService.listObjects(bucketName)
    }

    @GetMapping(value = ["/buckets/{bucketName}/{key}"])
    @ResponseStatus(value = HttpStatus.OK)
    fun `object`(@PathVariable bucketName: String, @PathVariable key: String): ByteArray {
        return storageService.getObject(bucketName, key)
    }
}
