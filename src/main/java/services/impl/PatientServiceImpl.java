package services.impl;

import model.Patient;
import model.User;
import model.dto.PatientDTO;
import model.dto.builders.AuthenticationDTOBuilder;
import model.dto.builders.PatientDTOBuilder;
import model.dto.request.SignupRequest;
import model.enums.PatientSex;
import model.enums.PatientType;
import persistence.PatientRepository;
import persistence.exceptions.RepositoryException;
import services.AccountsService;
import services.EmailService;
import services.PatientService;
import services.UserService;
import validation.PatientValidator;
import validation.utils.ValidatorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.function.Supplier;

@Service
public class PatientServiceImpl implements PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private AccountsService accountsService;

    @Autowired
    private PatientValidator patientValidator;

    @Autowired
    private EmailService emailService;

    @Override
    public Iterable<Patient> searchPatientsByName(String name) {
        return patientRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public Patient registerPatient(PatientDTO patientDTO) throws ValidatorException, RepositoryException {
        Patient patient = new Patient();
        patientValidator.validate(patientDTO);
        patient = PatientDTOBuilder.fromPatientDTO(patientDTO);
        if (Boolean.TRUE.equals(patientDTO.getUserAlreadyRegistered())) {

            User oldUser = userService.getUserById(patient.getOwner().getId());
            patient.getOwner().setPassword(oldUser.getPassword());
            patient.getOwner().setRole(oldUser.getRole());
            userService.updateUser(patient.getOwner());
        } else {

            SignupRequest signupRequest = AuthenticationDTOBuilder.toRegisterAccountDTO(patient.getOwner());
            String generatedPassword = accountsService.generatePassword();
            signupRequest.setPassword(generatedPassword);

            User savedUser = accountsService.registerUser(signupRequest);
            patient.getOwner().setId(savedUser.getId());
            patient.getOwner().setPassword(generatedPassword);
            emailService.sendPassword(patient.getOwner());
        }

        return patientRepository.save(patient);
    }

    @Override
    public Patient findPatientByDetails(
            String name, PatientSex sex, String type, String breed,
            String color, LocalDate birthDate, Float weight
    ) {
        return patientRepository.findByNameAndSexAndTypeAndBreedAndColorAndBirthDateAndWeight(name, sex, type, breed, color, birthDate, weight);
    }

    @Override
    public Iterable<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    @Override
    public Patient getPatientById(Integer id) throws RepositoryException {

        Supplier<RepositoryException> exception = () -> new RepositoryException("Patient not found");
        return patientRepository.findById(id).orElseThrow(exception);
    }

    @Override
    public Iterable<String> findDistinctTypes() {
        return patientRepository.findDistinctTypes();
    }

    @Override
    public Iterable<String> findDistinctBreeds() {
        return patientRepository.findDistinctBreeds();
    }

    @Override
    public Iterable<String> findDistinctSexes() {
        return patientRepository.findDistinctSexes();
    }

    @Override
    public Iterable<String> findDistinctColors() {
        return patientRepository.findDistinctColors();
    }

    @Override
    public Patient checkIfPatientExists(String patientName, PatientType patientType, String patientBreed, PatientSex patientSex, String ownerEmail) {
        return patientRepository.findByNameAndTypeAndBreedAndSexAndOwner_Email(
                patientName, patientType, patientBreed, patientSex, ownerEmail);
    }
}
