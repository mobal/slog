package hu.netcode.slog.data.dto.output

import java.time.ZonedDateTime

data class ObjectMetadataDto(
    val contentDisposition: String?,
    val contentEncoding: String?,
    val contentLanguage: String?,
    val contentLength: Long,
    val contentMD5: String?,
    val contentType: String,
    val expirationTime: ZonedDateTime?,
    val lastModified: ZonedDateTime
)
