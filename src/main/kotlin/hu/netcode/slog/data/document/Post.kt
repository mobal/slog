package hu.netcode.slog.data.document

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.ZonedDateTime
import javax.validation.constraints.NotEmpty

@Document(collection = "posts")
data class Post(
    @Id
    @JsonIgnore
    val id: Int = 0,
    @NotEmpty
    val author: String,
    val body: String,
    @NotEmpty
    val title: String,
    /* @JoinTable(
        inverseJoinColumns = [JoinColumn(name = "tag_id")],
        joinColumns = [JoinColumn(name = "post_id")],
        name = "posts_tags"
    )
    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER)
    val tagList: List<Tag>, */
    @JsonIgnore
    val visible: Boolean = true,
    val createdAt: ZonedDateTime = ZonedDateTime.now(),
    @JsonIgnore
    val deletedAt: ZonedDateTime? = null,
    @JsonIgnore
    val publishedAt: ZonedDateTime? = null,
    val updatedAt: ZonedDateTime = ZonedDateTime.now()
) {
    @JsonIgnore
    fun isDeleted(): Boolean {
        return deletedAt != null
    }

    @JsonIgnore
    fun isPublished(): Boolean {
        return publishedAt != null
    }

    /* val tags: List<String>
        get() = tagList.map { it.name }.toList() */
}
