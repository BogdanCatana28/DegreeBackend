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

        Patient patient = patientService.getPatientById(consultationDTO.getPatientId());

        User owner = userService.getUserByEmail(consultationDTO.getOwnerEmail());

        consultationValidator.validate(consultationDTO);

        Consultation consultation = ConsultationDTOBuilder.fromConsultationDTO(consultationDTO);

        patient.setWeight(consultationDTO.getPatientWeight());
        patient.setColor(consultationDTO.getPatientColor());
        owner.setPhone(consultationDTO.getOwnerPhone());
        owner.setAddress(consultationDTO.getOwnerAddress());
        consultation.setPatient(patient);
        consultation.setOwner(owner);

        return consultationRepository.save(consultation);
    }

    @Override
    public List<ConsultationDTO> getConsultationsByPatientId(Integer patientId) {
        List<Consultation> consultations = consultationRepository.findAllByPatientId(patientId);

        return (List<ConsultationDTO>) ConsultationDTOBuilder.toConsultationDTOList(consultations);
    }

    @Override
    public Consultation getConsultationById(Integer consultationId) throws RepositoryException {

        Supplier<RepositoryException> exception = () -> new RepositoryException("Consultation not found");
        return consultationRepository.findById(consultationId).orElseThrow(exception);
    }

    @Override
    public List<ConsultationDTO> getConsultationsByOwnerId(Integer ownerId) {
        List<Consultation> consultations = consultationRepository.findAllByOwnerId(ownerId);
        return (List<ConsultationDTO>) ConsultationDTOBuilder.toConsultationDTOList(consultations);
    }
}







