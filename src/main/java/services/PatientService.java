package services;

import model.Patient;
import model.dto.PatientDTO;
import model.enums.PatientSex;
import model.enums.PatientType;
import persistence.exceptions.RepositoryException;
import validation.utils.ValidatorException;

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
