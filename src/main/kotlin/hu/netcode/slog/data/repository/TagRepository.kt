package hu.netcode.slog.data.repository

import hu.netcode.slog.data.document.Tag
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface TagRepository : MongoRepository<Tag, Int>
