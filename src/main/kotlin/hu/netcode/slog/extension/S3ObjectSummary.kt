package hu.netcode.slog.extension

import com.amazonaws.services.s3.model.S3ObjectSummary
import hu.netcode.slog.data.dto.output.S3ObjectDto
import java.time.ZoneId
import java.time.ZonedDateTime

fun S3ObjectSummary.toS3ObjectDto(): S3ObjectDto {
    return S3ObjectDto(
        this.key,
        this.size,
        ZonedDateTime.ofInstant(this.lastModified.toInstant(), ZoneId.systemDefault())
    )
}
