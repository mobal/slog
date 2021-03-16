package hu.netcode.slog.controller

import hu.netcode.slog.data.dto.input.ObjectDto
import hu.netcode.slog.data.dto.output.BucketDto
import hu.netcode.slog.data.dto.output.ObjectMetadataDto
import hu.netcode.slog.data.dto.output.S3ObjectDto
import hu.netcode.slog.service.StorageService
import org.apache.logging.log4j.LogManager
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping(
    produces = [MediaType.APPLICATION_JSON_VALUE],
    value = ["/api/storage"]
)
@Validated
class StorageController(
    private val storageService: StorageService
) {
    private val logger = LogManager.getLogger(javaClass)

    @PostMapping(value = ["/buckets/{bucketName}"])
    @ResponseStatus(value = HttpStatus.CREATED)
    fun add(@RequestBody @Valid objectDto: ObjectDto): ResponseEntity<Unit> {
        return storageService.putObject(objectDto.bucketName, objectDto.key, objectDto.data, objectDto.mime)
    }

    @GetMapping(value = ["/buckets"])
    @ResponseStatus(value = HttpStatus.OK)
    fun buckets(): List<BucketDto> {
        return storageService.listBuckets()
    }

    @PostMapping(value = ["/copy"])
    @ResponseStatus(value = HttpStatus.OK)
    fun copy() {
        // TODO: Add implementation
    }

    @DeleteMapping(value = ["/buckets/{bucketName}/{key}"])
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    fun delete(@PathVariable bucketName: String, @PathVariable key: String): ResponseEntity<Unit> {
        return storageService.deleteObject(bucketName, key)
    }

    @PostMapping(value = ["/move"])
    @ResponseStatus(value = HttpStatus.OK)
    fun move() {
        // TODO: Add implementation
    }

    @GetMapping(value = ["/buckets/{bucketName}/{key}"])
    @ResponseStatus(value = HttpStatus.OK)
    fun `object`(@PathVariable bucketName: String, @PathVariable key: String): ResponseEntity<ByteArray> {
        return storageService.getObject(bucketName, key)
    }

    @GetMapping(value = ["/buckets/{bucketName}"])
    @ResponseStatus(value = HttpStatus.OK)
    fun objects(@PathVariable bucketName: String): List<S3ObjectDto> {
        return storageService.listObjects(bucketName)
    }

    @GetMapping(value = ["/buckets/{bucketName}/{key}/meta"])
    @ResponseStatus(value = HttpStatus.OK)
    fun meta(@PathVariable bucketName: String, @PathVariable key: String): ObjectMetadataDto {
        return storageService.getObjectMetaData(bucketName, key)
    }
}
