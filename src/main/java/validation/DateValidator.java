package validation;

import validation.utils.ValidatorException;

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
}
