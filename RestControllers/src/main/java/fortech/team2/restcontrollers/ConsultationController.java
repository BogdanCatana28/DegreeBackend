package fortech.team2.restcontrollers;

import fortech.team2.model.Consultation;
import fortech.team2.model.dto.ConsultationDTO;
import fortech.team2.model.dto.builders.ConsultationDTOBuilder;
import fortech.team2.persistence.exceptions.RepositoryException;
import fortech.team2.services.ConsultationService;
import fortech.team2.validation.utils.ValidatorException;
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

    /**
     * You could also declare this autowired field as 'final'
     */
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
    /**
     * You already have 'consultations' in url
     */
    @GetMapping("/consultations/by-patient/{patientId}")
    public ResponseEntity<List<ConsultationDTO>> getConsultationsByPatientId(@PathVariable Integer patientId) {
        List<ConsultationDTO> consultations = consultationService.getConsultationsByPatientId(patientId);
        return ResponseEntity.ok(consultations);
    }
    @PreAuthorize("hasRole('MEDIC')")
    /**
     * You already have 'consultations' in url
     */
    @GetMapping("/consultations/{consultationId}")
    public ResponseEntity<ConsultationDTO> getConsultation(@PathVariable Integer consultationId) {
        try {
            return ResponseEntity.ok(ConsultationDTOBuilder.toConsultationDTO(consultationService.getConsultationById(consultationId)));
        } catch (RepositoryException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }
}

