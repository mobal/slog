package hu.netcode.slog.data.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.ZonedDateTime
import java.util.UUID
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
    val id: Int? = null,
    @Column(name = "user_id")
    @JsonIgnore
    val userId: String = UUID.randomUUID().toString(),
    @Column(name = "display_name")
    @get:JsonProperty(value = "name")
    val displayName: String? = null,
    @JsonIgnore
    val email: String,
    @JsonIgnore
    val name: String,
    @JsonIgnore
    val password: String,
    @JsonIgnore
    val username: String,
    @Column(name = "created_at")
    val createdAt: ZonedDateTime = ZonedDateTime.now(),
    @Column(name = "deleted_at")
    @JsonIgnore
    val deletedAt: ZonedDateTime? = null,
    @JsonIgnore
    val activation: String? = null
) {
    @JsonIgnore
    fun isActive(): Boolean {
        return !activation.isNullOrEmpty()
    }

    @JsonIgnore
    fun isDeleted(): Boolean {
        return deletedAt != null
    }
}
