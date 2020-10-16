package hu.netcode.slog.data.document

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.ZonedDateTime

@Document(collection = "tags")
data class Tag(
    @Id
    val id: Int,
    val name: String,
    val description: String,
    val createdAt: ZonedDateTime,
    val deletedAt: ZonedDateTime
)
