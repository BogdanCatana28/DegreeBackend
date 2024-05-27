package fortech.team2.services.impl;

import fortech.team2.model.Patient;
import fortech.team2.model.User;
import fortech.team2.model.dto.PatientDTO;
import fortech.team2.model.dto.builders.AuthenticationDTOBuilder;
import fortech.team2.model.dto.builders.PatientDTOBuilder;
import fortech.team2.model.dto.request.SignupRequest;
import fortech.team2.model.enums.PatientSex;
import fortech.team2.model.enums.PatientType;
import fortech.team2.persistence.PatientRepository;
import fortech.team2.persistence.exceptions.RepositoryException;
import fortech.team2.services.AccountsService;
import fortech.team2.services.EmailService;
import fortech.team2.services.PatientService;
import fortech.team2.services.UserService;
import fortech.team2.validation.PatientValidator;
import fortech.team2.validation.utils.ValidatorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.function.Supplier;

@Service
public class PatientServiceImpl implements PatientService {

    /**
     * You could also declare these autowired fields as 'final'
     */
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
            /**
             * Extract the next 4 lines into a separated method because the S (single responisibility) principle is violated
             * Call this method here atfter doing that
             */
            User oldUser = userService.getUserById(patient.getOwner().getId());
            patient.getOwner().setPassword(oldUser.getPassword());
            patient.getOwner().setRole(oldUser.getRole());
            userService.updateUser(patient.getOwner());
        } else {
            /**
             * Extract the next 3 lines into a separated method because the S (single responisibility) principle is violated
             * Call this method here atfter doing that
             */
            SignupRequest signupRequest = AuthenticationDTOBuilder.toRegisterAccountDTO(patient.getOwner());
            String generatedPassword = accountsService.generatePassword();
            signupRequest.setPassword(generatedPassword);
            /**
             * Extract the next 4 lines into a separated method because the S (single responisibility) principle is violated
             * Call this method here atfter doing that
             */
            User savedUser = accountsService.registerUser(signupRequest);
            patient.getOwner().setId(savedUser.getId());
            patient.getOwner().setPassword(generatedPassword);
            emailService.sendPassword(patient.getOwner());
        }
        /**
         * You should do the save into a try-catch block because of the errors that can appear
         */
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
        /**
         * You should extract this message as a constant in a separated class named 'Constants' in 'utils' package , as public static final
         */
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
