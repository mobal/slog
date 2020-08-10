package hu.netcode.slog.data.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import java.time.ZonedDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "users")
data class User(
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @JsonIgnore
    val id: Int,
    @Column(name = "user_id")
    @JsonIgnore
    val userId: String,
    @Column(name = "display_name")
    val displayName: String,
    val name: String,
    val username: String,
    @Column(name = "created_at")
    val createdAt: ZonedDateTime,
    @Column(name = "deleted_at")
    @JsonIgnore
    val deletedAt: ZonedDateTime? = null
)
