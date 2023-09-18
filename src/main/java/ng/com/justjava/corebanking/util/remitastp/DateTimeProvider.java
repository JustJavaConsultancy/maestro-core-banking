package ng.com.justjava.corebanking.util.remitastp;

import org.springframework.stereotype.Component;

import java.time.*;

@Component
public class DateTimeProvider {

    public Instant getStartOfDay(String timeZone, LocalDate date) {
        ZoneOffset offset = ZoneId.of(timeZone).getRules().getOffset(date.atStartOfDay());
        return LocalDateTime.of(date, LocalTime.MIN).toInstant(offset);
    }

    public Instant getEndOfDay(String timeZone, LocalDate date) {
        ZoneOffset offset = ZoneId.of(timeZone).getRules().getOffset(date.atStartOfDay());
        return LocalDateTime.of(date, LocalTime.MAX).toInstant(offset);
    }


    public Instant getInstant(String timeZone, LocalDateTime date) {
        ZoneOffset offset = ZoneId.of(timeZone).getRules().getOffset(date);
        return date.toInstant(offset);
    }

    public LocalDate plusDays(String timeZone, LocalDate bankingDate, int days) {
        ZoneOffset offset = ZoneId.of(timeZone).getRules().getOffset(bankingDate.atStartOfDay());
        return bankingDate.plusDays(days).atStartOfDay(offset).toLocalDate();
    }

    public LocalDate getLocalDateNow(String timeZone) {
        return LocalDate.now(ZoneId.of(timeZone));
//        ZoneOffset offset = ZoneId.of(timeZone).getRules().getOffset(now.atStartOfDay());
//        return now.atStartOfDay(offset).toLocalDate();
    }

    public LocalDate getLocalDate(String timeZone, LocalDate date) {
//                ZoneOffset offset = ZoneId.of(timeZone).getRules().getOffset(date.);
//        ZonedDateTime.t
        return date;
    }

    public Instant getInstantNow(String timeZone) {
        LocalDateTime date = LocalDateTime.now();
        ZoneOffset offset = ZoneId.of(timeZone).getRules().getOffset(date);
        return date.toInstant(offset);
    }
}
