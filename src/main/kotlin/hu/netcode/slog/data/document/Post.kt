package hu.netcode.slog.data.document

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import hu.netcode.slog.serializer.ZonedDateTimeSerializer
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.ZonedDateTime
import javax.validation.Valid
import javax.validation.constraints.NotEmpty

@Document(collection = "posts")
data class Post(
    @Id
    @JsonIgnore
    val id: String? = null,
    @NotEmpty
    var author: String,
    var body: String,
    @NotEmpty
    @Valid
    val meta: Meta,
    @NotEmpty
    var title: String,
    @JsonIgnore
    @NotEmpty
    var tagList: List<String>,
    @JsonIgnore
    val visible: Boolean = true,
    @JsonIgnore
    val createdAt: ZonedDateTime? = ZonedDateTime.now(),
    @JsonIgnore
    val deletedAt: ZonedDateTime? = null,
    @JsonSerialize(using = ZonedDateTimeSerializer::class)
    val publishedAt: ZonedDateTime? = null,
    @JsonSerialize(using = ZonedDateTimeSerializer::class)
    var updatedAt: ZonedDateTime = ZonedDateTime.now()
) {
    @JsonIgnore
    fun isDeleted(): Boolean {
        return deletedAt != null
    }

    @JsonIgnore
    fun isPublished(): Boolean {
        return publishedAt != null
    }
}
