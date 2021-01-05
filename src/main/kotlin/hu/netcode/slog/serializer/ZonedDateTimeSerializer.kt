package hu.netcode.slog.serializer

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class ZonedDateTimeSerializer : JsonSerializer<ZonedDateTime>() {
    override fun serialize(value: ZonedDateTime?, gen: JsonGenerator?, serializers: SerializerProvider?) {
        gen?.writeString(DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(value))
    }
}
