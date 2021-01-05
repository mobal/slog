package hu.netcode.slog.data.document

import org.springframework.data.mongodb.core.mapping.Document
import javax.validation.constraints.Min
import javax.validation.constraints.NotEmpty

@Document
data class Meta(
    @Min(value = 0)
    @NotEmpty
    val readingTime: Int,
    @NotEmpty
    val slug: String
)
