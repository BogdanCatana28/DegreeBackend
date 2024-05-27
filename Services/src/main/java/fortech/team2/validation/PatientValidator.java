package fortech.team2.validation;

import fortech.team2.model.Patient;
import fortech.team2.model.dto.PatientDTO;
import fortech.team2.model.enums.PatientSex;
import fortech.team2.model.enums.PatientType;
import fortech.team2.validation.utils.ValidatorException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class PatientValidator {
    public void validate(Patient patient) throws ValidatorException {
        validateSex(patient.getSex().toString());
        validateBreed(patient.getBreed());
        validateColor(patient.getColor());
        validateWeight(patient.getWeight());
        validateType(patient.getType().toString());
        validateName(patient.getName());
        validateBirthDate(patient.getBirthDate());
    }

    public void validate(PatientDTO patientDTO) throws ValidatorException {
        validateSex(patientDTO.getPatientSex());
        validateBreed(patientDTO.getPatientBreed());
        validateColor(patientDTO.getPatientColour());
        validateWeight(patientDTO.getPatientWeight());
        validateType(patientDTO.getPatientType());
        validateName(patientDTO.getPatientName());
        validateBirthDate(patientDTO.getPatientBirthdate());

        validateAlreadyRegistered(patientDTO.getUserAlreadyRegistered(), patientDTO.getOwner().getId());
    }

    public void validateAlreadyRegistered(Boolean alreadyRegistered, Integer ownerId) throws ValidatorException {
        if (alreadyRegistered != null && (alreadyRegistered)) {
            validateOwnerId(ownerId);
        }
    }

    public void validateOwnerId(Integer ownerId) throws ValidatorException {
        if (ownerId == null) {
            throw new ValidatorException("Id cannot be empty");
        }
    }

    public void validateName(String name) throws ValidatorException {
        if (name == null || name.isEmpty()) {
            throw new ValidatorException("Name cannot be empty");
        }

        if (!name.matches("^[A-Za-z -]+$")) {
            throw new ValidatorException("Name can't contain digits!");
        }
    }

    public void validateBirthDate(LocalDate birthDate) throws ValidatorException {
        if (birthDate == null) {
            return;
        }
        if (birthDate.getYear() > LocalDate.now().getYear()) {
            throw new ValidatorException("Birth date cannot be in the future");
        }
        if (birthDate.getYear() == LocalDate.now().getYear() && birthDate.getMonthValue() > LocalDate.now().getMonthValue()) {
            throw new ValidatorException("Birth date cannot be in the future");
        }
    }

    public void validateType(String type) throws ValidatorException {
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

    public void validateWeight(Float weight) throws ValidatorException {
        if (weight == null) {
            throw new ValidatorException("Weight cannot be empty");
        }
        if (weight < 0) {
            throw new ValidatorException("Weight cannot be negative");
        }
    }

    public void validateColor(String color) throws ValidatorException {
        if (color == null || color.isEmpty()) {
            throw new ValidatorException("Color cannot be empty");
        }
        if (!color.chars().allMatch(Character::isLetter)) {
            throw new ValidatorException("Color can't contain digits");
        }
    }

    public void validateBreed(String breed) throws ValidatorException {
        if (breed == null || breed.isEmpty()) {
            throw new ValidatorException("Breed cannot be empty");
        }
        if (!breed.matches("^[A-Za-z -]+$")) {
            throw new ValidatorException("Breed can't contain digits!");
        }
    }

    public void validateSex(String sex) throws ValidatorException {
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
}
