package hu.netcode.slog.data.document

import org.springframework.data.mongodb.core.mapping.Document
import javax.validation.constraints.NotEmpty

@Document
data class Meta(
    @NotEmpty
    val slug: String
)
