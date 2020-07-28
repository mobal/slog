package hu.netcode.slog.data.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table
import javax.validation.constraints.NotEmpty

@Entity
@Table(name = "metas")
data class Meta(
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    val id: Int = 0,
    @NotEmpty
    val postId: Int = 0,
    @NotEmpty
    val slug: String,
    var views: Int = 0
)
