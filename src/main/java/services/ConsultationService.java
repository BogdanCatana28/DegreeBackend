package services;

import model.Consultation;
import model.dto.ConsultationDTO;
import persistence.exceptions.RepositoryException;
import validation.utils.ValidatorException;

import java.util.List;

public interface ConsultationService {
    Consultation addConsultation(ConsultationDTO consultationDTO) throws ValidatorException, RepositoryException;

    List<ConsultationDTO> getConsultationsByPatientId(Integer patientId);

    Consultation getConsultationById(Integer consultationId) throws RepositoryException;
}

