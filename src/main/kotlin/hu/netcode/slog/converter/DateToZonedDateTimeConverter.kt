package hu.netcode.slog.converter

import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.ReadingConverter
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.Date

@ReadingConverter
class DateToZonedDateTimeConverter : Converter<Date, ZonedDateTime> {
    override fun convert(source: Date): ZonedDateTime? {
        return source.toInstant().atZone(ZoneId.systemDefault())
    }
}
