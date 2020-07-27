package hu.netcode.slog.data.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "metas")
data class Meta(
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    val id: Int,
    val slug: String,
    val views: Int
)
