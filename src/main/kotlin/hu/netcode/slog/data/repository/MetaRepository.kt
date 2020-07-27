package hu.netcode.slog.data.repository

import hu.netcode.slog.data.entity.Meta
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MetaRepository : JpaRepository<Meta, Int>
