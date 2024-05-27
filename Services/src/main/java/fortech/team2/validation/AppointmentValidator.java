package fortech.team2.validation;

import fortech.team2.model.dto.AppointmentDTO;
import fortech.team2.model.enums.PatientSex;
import fortech.team2.model.enums.PatientType;
import fortech.team2.validation.utils.ValidatorException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class AppointmentValidator {
    public void validate(AppointmentDTO appointmentDTO) throws ValidatorException {
        validateSex(appointmentDTO.getPatientSex());
        validateName(appointmentDTO.getPatientName());
        validateBirthDate(appointmentDTO.getPatientAge());
        validateColour(appointmentDTO.getPatientColour());
        validateBreed(appointmentDTO.getPatientBreed());
        validateType(appointmentDTO.getPatientType());
    }

    private void validateSex(String sex) throws ValidatorException {
        if (sex == null || sex.isEmpty()) {
            throw new ValidatorException("Sex cannot be empty");
        }
        if (!sex.chars().allMatch(Character::isLetter)) {
            throw new ValidatorException("Sex can't contain digits");
        }
        try {
            PatientSex.valueOf(sex.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ValidatorException("Invalid value for sex");
        }
    }

    private void validateType(String type) throws ValidatorException {
        if (type == null || type.isEmpty()) {
            throw new ValidatorException("Type cannot be empty");
        }
        if (!type.chars().allMatch(Character::isLetter)) {
            throw new ValidatorException("Type can't contain digits");
        }
        try {
            PatientType.valueOf(type.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ValidatorException("Invalid value for type");
        }
    }

    private void validateName(String name) throws ValidatorException {
        if (name == null || name.isEmpty()) {
            throw new ValidatorException("Name cannot be empty");
        }

        if (!name.matches("^[A-Za-z -]+$")) {
            throw new ValidatorException("Name can't contain digits!");
        }
    }

    private void validateBirthDate(Integer months) throws ValidatorException {
        if (months == null) {
            return;
        }
        LocalDate birthDate = LocalDate.now().minusYears(months);
        if (birthDate.getYear() > LocalDate.now().getYear()) {
            throw new ValidatorException("Birth date cannot be in the future");
        }
        if (birthDate.getYear() == LocalDate.now().getYear() && birthDate.getMonthValue() > LocalDate.now().getMonthValue()) {
            throw new ValidatorException("Birth date cannot be in the future");
        }
    }

    private void validateColour(String colour) throws ValidatorException {
        if (colour == null || colour.isEmpty()) {
            return;
        }
        if (!colour.chars().allMatch(Character::isLetter)) {
            throw new ValidatorException("Colour can't contain digits");
        }
    }

    private void validateBreed(String breed) throws ValidatorException {
        if (breed == null || breed.isEmpty()) {
            throw new ValidatorException("Breed cannot be empty");
        }
        if (!breed.matches("^[A-Za-z -]+$")) {
            throw new ValidatorException("Breed can't contain digits!");
        }
    }
}
