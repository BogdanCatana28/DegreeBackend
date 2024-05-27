package fortech.team2.services;

import fortech.team2.model.Consultation;
import fortech.team2.model.dto.ConsultationDTO;
import fortech.team2.persistence.exceptions.RepositoryException;
import fortech.team2.validation.utils.ValidatorException;

import java.util.List;

public interface ConsultationService {
    Consultation addConsultation(ConsultationDTO consultationDTO) throws ValidatorException, RepositoryException;

    List<ConsultationDTO> getConsultationsByPatientId(Integer patientId);

    Consultation getConsultationById(Integer consultationId) throws RepositoryException;
}

