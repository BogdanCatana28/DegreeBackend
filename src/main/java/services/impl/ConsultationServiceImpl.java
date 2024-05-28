package services.impl;

import model.Consultation;
import model.Patient;
import model.User;
import model.dto.ConsultationDTO;
import model.dto.builders.ConsultationDTOBuilder;
import persistence.ConsultationRepository;
import persistence.exceptions.RepositoryException;
import services.ConsultationService;
import services.PatientService;
import services.UserService;
import validation.ConsultationValidator;
import validation.utils.ValidatorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Supplier;

@Service
public class ConsultationServiceImpl implements ConsultationService {

    /**
     * You could also declare these autowired fields as 'final'
     */
    @Autowired
    private ConsultationRepository consultationRepository;

    @Autowired
    private PatientService patientService;

    @Autowired
    private UserService userService;

    @Autowired
    private ConsultationValidator consultationValidator;

    @Override
    public Consultation addConsultation(ConsultationDTO consultationDTO) throws ValidatorException, RepositoryException {

        /**
         * Move the instantion of 'dayOff' object at line 57.
         * If an error will occur in 57-62 lines, this allocation was made in vain
         */
        Patient patient = patientService.getPatientById(consultationDTO.getPatientId());

        User owner = userService.getUserByEmail(consultationDTO.getOwnerEmail());

        consultationValidator.validate(consultationDTO);

        Consultation consultation = ConsultationDTOBuilder.fromConsultationDTO(consultationDTO);

        /**
         * Extract these lines into a separate builder class
         */
        patient.setWeight(consultationDTO.getPatientWeight());
        patient.setColor(consultationDTO.getPatientColor());
        owner.setPhone(consultationDTO.getOwnerPhone());
        owner.setAddress(consultationDTO.getOwnerAddress());
        consultation.setPatient(patient);
        consultation.setOwner(owner);

        /**
         * You should do the save into a try-catch block because of the errors that can appear
         */
        return consultationRepository.save(consultation);
    }

    @Override
    public List<ConsultationDTO> getConsultationsByPatientId(Integer patientId) {
        List<Consultation> consultations = consultationRepository.findAllByPatientId(patientId);
        /**
         * I don't like this casting
         */
        return (List<ConsultationDTO>) ConsultationDTOBuilder.toConsultationDTOList(consultations);
    }

    @Override
    public Consultation getConsultationById(Integer consultationId) throws RepositoryException {
        /**
         * You should extract this message as a constant in a separated class named 'Constants' in 'utils' package , as public static final
         */
        Supplier<RepositoryException> exception = () -> new RepositoryException("Consultation not found");
        return consultationRepository.findById(consultationId).orElseThrow(exception);
    }
}







