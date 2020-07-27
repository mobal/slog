package hu.netcode.slog.data.repository

import hu.netcode.slog.data.entity.Post
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PostRepository : JpaRepository<Post, Int>
