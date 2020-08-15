package hu.netcode.slog.data.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import java.time.ZonedDateTime
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
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
import org.hibernate.annotations.Type

@Entity
@Table(name = "posts")
data class Post(
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
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
    @OneToMany
    val tagList: List<Tag>,
    @JsonIgnore
    val user: User? = null,
    @Column(columnDefinition = "TINYINT", name = "visible")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    val visible: Boolean = true,
    @Column(name = "created_at")
    val createdAt: ZonedDateTime = ZonedDateTime.now(),
    @Column(name = "deleted_at")
    @JsonIgnore
    val deletedAt: ZonedDateTime? = null,
    @Column(name = "published_at")
    val publishedAt: ZonedDateTime? = null
) {
    @JsonIgnore
    fun isDeleted(): Boolean {
        return deletedAt != null
    }

    val tags: List<String>
        get() = tagList.map { it.name }.toList()
}
