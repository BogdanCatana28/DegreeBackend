package fortech.team2.model.dto.builders;

import fortech.team2.model.Patient;
import fortech.team2.model.dto.PatientDTO;
import fortech.team2.model.enums.PatientSex;
import fortech.team2.model.enums.PatientType;

import java.time.LocalDate;
import java.util.ArrayList;

public class PatientDTOBuilder {
    private PatientDTOBuilder() {
    }

    /**
     * Simply name 'toEntity'
     */
    public static Patient fromPatientDTO(PatientDTO patientDTO) throws IllegalArgumentException {
        return Patient.builder()
                .id(patientDTO.getId())
                .name(patientDTO.getPatientName())
                .sex(PatientSex.valueOf(patientDTO.getPatientSex().trim().toUpperCase()))
                .type(PatientType.valueOf(patientDTO.getPatientType().trim().toUpperCase()))
                .breed(patientDTO.getPatientBreed())
                .color(patientDTO.getPatientColour())
                .birthDate((patientDTO.getPatientBirthdate() != null) ? patientDTO.getPatientBirthdate() : convertToBirthDate(patientDTO))
                .weight(patientDTO.getPatientWeight())
                .owner(UserDTOBuilder.fromUserDTO(patientDTO.getOwner()))
                .medicalHistory(patientDTO.getPatientMedicalHistoryBeforeClinic())
                .build();
    }

    /**
     * Simply name 'toDTO'
     */
    public static PatientDTO toPatientDTO(Patient patient) {
        PatientDTO patientDTO = PatientDTO.builder()
                .id(patient.getId())
                .patientName(patient.getName())
                .patientSex(patient.getSex().toString())
                .patientType(patient.getType().toString())
                .patientBreed(patient.getBreed())
                .patientColour(patient.getColor())
                .patientWeight(patient.getWeight())
                .owner(UserDTOBuilder.toUserDTO(patient.getOwner()))
                .patientMedicalHistoryBeforeClinic(patient.getMedicalHistory())
                .build();

        PatientDTO patientDTOWithBirthDate = convertFromBirthDate(patient);

        if (patientDTOWithBirthDate != null) {
            patientDTO.setPatientAgeYears(patientDTOWithBirthDate.getPatientAgeYears());
            patientDTO.setPatientAgeMonths(patientDTOWithBirthDate.getPatientAgeMonths());
            patientDTO.setPatientBirthdate(patientDTOWithBirthDate.getPatientBirthdate());
        }

        return patientDTO;
    }


    private static LocalDate convertToBirthDate(PatientDTO patientDTO) {
        if (patientDTO.getPatientAgeYears() == null && patientDTO.getPatientAgeMonths() == null) {
            return null;
        }

        Integer age = patientDTO.getPatientAgeYears();
        Integer month = patientDTO.getPatientAgeMonths();

        return LocalDate.now().minusYears(age).minusMonths(month);
    }

    private static PatientDTO convertFromBirthDate(Patient patient) {
        LocalDate birthDate = patient.getBirthDate();

        if (birthDate == null) {
            return null;
        }

        Integer age = LocalDate.now().getYear() - birthDate.getYear();
        Integer month = LocalDate.now().getMonthValue() - birthDate.getMonthValue();

        return PatientDTO.builder()
                .patientAgeYears(age)
                .patientAgeMonths(month)
                .patientBirthdate(birthDate)
                .build();
    }

    public static Iterable<PatientDTO> toPatientDTOList(Iterable<Patient> patients) {
        ArrayList<PatientDTO> patientDTOS = new ArrayList<>();
        patients.forEach(patient -> patientDTOS.add(toPatientDTO(patient)));

        return patientDTOS;
    }
}
