package hu.netcode.slog.data.document

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime
import javax.validation.Valid
import javax.validation.constraints.NotEmpty

@Document(collection = "posts")
data class Post(
    @Id
    @JsonIgnore
    val id: String? = null,
    @NotEmpty
    val author: String,
    val body: String,
    @NotEmpty
    @Valid
    val meta: Meta,
    @NotEmpty
    val title: String,
    @JsonIgnore
    @NotEmpty
    val tagList: List<String>,
    @JsonIgnore
    val visible: Boolean = true,
    @JsonIgnore
    val createdAt: LocalDateTime = LocalDateTime.now(),
    @JsonIgnore
    val deletedAt: LocalDateTime? = null,
    val publishedAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime = LocalDateTime.now()
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
