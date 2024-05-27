package fortech.team2.services;

import fortech.team2.model.Patient;
import fortech.team2.model.dto.PatientDTO;
import fortech.team2.model.enums.PatientSex;
import fortech.team2.model.enums.PatientType;
import fortech.team2.persistence.exceptions.RepositoryException;
import fortech.team2.validation.utils.ValidatorException;

import java.time.LocalDate;

public interface PatientService {

    Patient findPatientByDetails(
            String name, PatientSex gender, String type, String breed,
            String color, LocalDate birthDate, Float weight
    );

    Patient registerPatient(PatientDTO patientDTO) throws ValidatorException, RepositoryException;

    Patient getPatientById(Integer id) throws RepositoryException;

    Iterable<Patient> getAllPatients();

    Iterable<Patient> searchPatientsByName(String name);

    Iterable<String> findDistinctTypes();

    Iterable<String> findDistinctBreeds();

    Iterable<String> findDistinctSexes();

    Iterable<String> findDistinctColors();

    Patient checkIfPatientExists(String patientName, PatientType patientType, String patientBreed, PatientSex patientSex, String ownerEmail);

}
