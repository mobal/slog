package hu.netcode.slog.extension

import com.amazonaws.services.s3.model.ObjectMetadata
import hu.netcode.slog.data.dto.output.ObjectMetadataDto
import java.time.ZoneId
import java.time.ZonedDateTime

fun ObjectMetadata.toObjectMetaDataDto(): ObjectMetadataDto =
    ObjectMetadataDto(
        contentDisposition,
        contentEncoding,
        contentLanguage,
        contentLength,
        contentMD5,
        contentType,
        expirationTime?.let {
            ZonedDateTime.ofInstant(it.toInstant(), ZoneId.systemDefault())
        },
        ZonedDateTime.ofInstant(lastModified.toInstant(), ZoneId.systemDefault())
    )
