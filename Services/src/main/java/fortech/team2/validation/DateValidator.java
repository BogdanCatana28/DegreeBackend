package fortech.team2.validation;

import fortech.team2.validation.utils.ValidatorException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

public class DateValidator {
    private DateValidator() {
    }

    public static void validateAgainstCurrentDate(LocalDate date) throws ValidatorException {
        if (date.isBefore(LocalDate.now())) {
            throw new ValidatorException("Date cannot be in the past");
        }
    }

    public static void validateAgainstCurrentDate(LocalDateTime date) throws ValidatorException {
        if (date.isBefore(LocalDateTime.now())) {
            throw new ValidatorException("Date cannot be in the past");
        }
    }

    public static void validateBeforeEndOfNextWeek(LocalDate date) throws ValidatorException {
        if (date.isAfter(getEndOfNextWeekDate(LocalDate.now())) || date.isEqual(getEndOfNextWeekDate(LocalDate.now()))) {
            throw new ValidatorException("Date cannot be later than the end of next week");
        }
    }

    public static void validateAfterEndOfNextWeek(LocalDate date) throws ValidatorException {
        if (date.isBefore(getEndOfNextWeekDate(LocalDate.now())) || date.isEqual(getEndOfNextWeekDate(LocalDate.now()))) {
            throw new ValidatorException("Date cannot be sooner than the end of next week");
        }
    }

    private static LocalDate getEndOfNextWeekDate(LocalDate date) {
        return date.plusWeeks(1).with(TemporalAdjusters.nextOrSame(DayOfWeek.FRIDAY));
    }

}
