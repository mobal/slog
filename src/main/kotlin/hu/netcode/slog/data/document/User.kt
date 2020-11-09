package hu.netcode.slog.data.document

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.ZonedDateTime
import java.util.UUID

@Document(collection = "users")
data class User(
    @Id
    @JsonIgnore
    val id: String? = null,
    @JsonIgnore
    val userId: String = UUID.randomUUID().toString(),
    @get:JsonProperty(value = "name")
    val displayName: String? = null,
    var email: String,
    @JsonIgnore
    var name: String,
    @JsonIgnore
    var password: String,
    @JsonIgnore
    var username: String,
    val createdAt: ZonedDateTime = ZonedDateTime.now(),
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
