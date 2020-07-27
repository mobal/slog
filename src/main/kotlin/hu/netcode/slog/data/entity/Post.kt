package hu.netcode.slog.data.entity

import java.time.ZonedDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Lob
import javax.persistence.Table
import javax.validation.constraints.NotEmpty

@Entity
@Table(name = "posts")
data class Post(
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    val id: Int,
    @NotEmpty
    val author: String,
    @Lob
    val body: String,
    @Column(name = "created_at")
    val createdAt: ZonedDateTime,
    @Column(name = "deleted_at")
    val deletedAt: ZonedDateTime? = null
)
