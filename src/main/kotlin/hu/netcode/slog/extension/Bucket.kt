package hu.netcode.slog.extension

import com.amazonaws.services.s3.model.Bucket
import hu.netcode.slog.data.dto.output.BucketDto
import java.time.ZoneId
import java.time.ZonedDateTime

fun Bucket.toBucketDto(): BucketDto {
    return BucketDto(this.name, ZonedDateTime.ofInstant(this.creationDate.toInstant(), ZoneId.systemDefault()))
}
