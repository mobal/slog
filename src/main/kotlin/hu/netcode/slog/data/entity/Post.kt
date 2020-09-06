package hu.netcode.slog.data.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.Type
import java.time.ZonedDateTime
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.Lob
import javax.persistence.OneToMany
import javax.persistence.OneToOne
import javax.persistence.Table
import javax.validation.constraints.NotEmpty

@Entity
@Table(name = "posts")
data class Post(
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @JsonIgnore
    val id: Int = 0,
    @NotEmpty
    val author: String,
    @Lob
    val body: String,
    @JoinColumn(name = "id", referencedColumnName = "id")
    @OneToOne(cascade = [CascadeType.PERSIST])
    val meta: Meta,
    @NotEmpty
    val title: String,
    @JoinTable(
        inverseJoinColumns = [JoinColumn(name = "tag_id")],
        joinColumns = [JoinColumn(name = "post_id")],
        name = "posts_tags"
    )
    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER)
    val tagList: List<Tag>,
    @Column(columnDefinition = "TINYINT", name = "visible")
    @JsonIgnore
    @Type(type = "org.hibernate.type.NumericBooleanType")
    val visible: Boolean = true,
    @Column(name = "created_at")
    val createdAt: ZonedDateTime = ZonedDateTime.now(),
    @Column(name = "deleted_at")
    @JsonIgnore
    val deletedAt: ZonedDateTime? = null,
    @Column(name = "published_at")
    @JsonIgnore
    val publishedAt: ZonedDateTime? = null,
    @Column(name = "updated_at")
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

    val tags: List<String>
        get() = tagList.map { it.name }.toList()
}
