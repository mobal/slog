package hu.netcode.slog.data.repository

import hu.netcode.slog.data.entity.Tag
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TagRepository : JpaRepository<Tag, Int>
