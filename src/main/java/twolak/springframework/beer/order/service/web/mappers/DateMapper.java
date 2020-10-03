package twolak.springframework.beer.order.service.web.mappers;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.springframework.stereotype.Component;

/**
 *
 * @author twolak
 */
@Component
public class DateMapper {
    
    public OffsetDateTime asOffsetDateTime(Timestamp timestamp) {
        if (timestamp != null) {
            LocalDateTime local = timestamp.toLocalDateTime();
            return OffsetDateTime.of(local.getYear(), local.getMonthValue(),
                    local.getDayOfMonth(), local.getHour(), local.getMinute(),
                    local.getSecond(), local.getNano(), ZoneOffset.UTC);
        } else {
            return null;
        }
    }

    public Timestamp asTimestamp(OffsetDateTime offsetDateTime) {
        if (offsetDateTime != null) {
            return Timestamp.valueOf(offsetDateTime.atZoneSameInstant(ZoneOffset.UTC).toLocalDateTime());
        } else {
            return null;
        }
    }
}
