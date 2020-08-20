package hu.netcode.slog.controller

import hu.netcode.slog.service.S3Service
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(
    produces = [MediaType.APPLICATION_JSON_VALUE],
    value = ["/api/s3"]
)@Validated
class S3Controller(
    private val s3Service: S3Service
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @GetMapping(value = ["buckets"])
    @ResponseStatus(value = HttpStatus.OK)
    fun buckets(): List<String> {
        return s3Service.listBuckets()
    }
}
