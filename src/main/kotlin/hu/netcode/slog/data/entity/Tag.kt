package hu.netcode.slog.data.entity

import java.time.ZonedDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "tags")
data class Tag(
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    val id: Int,
    val name: String,
    val description: String,
    @Column(name = "created_at")
    val createdAt: ZonedDateTime,
    @Column(name = "deleted_at")
    val deletedAt: ZonedDateTime
)
