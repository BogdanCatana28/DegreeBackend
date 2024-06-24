package controllers;

import model.Consultation;
import model.dto.ConsultationDTO;
import model.dto.builders.ConsultationDTOBuilder;
import persistence.exceptions.RepositoryException;
import services.ConsultationService;
import validation.utils.ValidatorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/consultations")
public class ConsultationController {

    @Autowired
    private ConsultationService consultationService;

    @PostMapping
    @PreAuthorize("hasRole('MEDIC')")
    public ResponseEntity<ConsultationDTO> addConsultation(@RequestBody ConsultationDTO consultationDTO) {
        try {
            Consultation consultation = consultationService.addConsultation(consultationDTO);
            return ResponseEntity.ok(ConsultationDTOBuilder.toConsultationDTO(consultation));
        } catch (ValidatorException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (RepositoryException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);

        }
    }
    @PreAuthorize("hasRole('MEDIC')")
    @GetMapping("/by-patient/{patientId}")
    public ResponseEntity<List<ConsultationDTO>> getConsultationsByPatientId(@PathVariable Integer patientId) {
        List<ConsultationDTO> consultations = consultationService.getConsultationsByPatientId(patientId);
        return ResponseEntity.ok(consultations);
    }
    @PreAuthorize("hasRole('MEDIC')")
    @GetMapping("/{consultationId}")
    public ResponseEntity<ConsultationDTO> getConsultation(@PathVariable Integer consultationId) {
        try {
            return ResponseEntity.ok(ConsultationDTOBuilder.toConsultationDTO(consultationService.getConsultationById(consultationId)));
        } catch (RepositoryException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }
}

