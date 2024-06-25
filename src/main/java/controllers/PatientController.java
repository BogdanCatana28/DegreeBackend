package controllers;

import model.Patient;
import model.dto.PatientDTO;
import model.dto.builders.PatientDTOBuilder;
import model.dto.response.PatientExistsResponse;
import model.enums.PatientSex;
import model.enums.PatientType;
import persistence.exceptions.RepositoryException;
import services.PatientService;
import validation.utils.ValidatorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;

@RestController
@CrossOrigin
@RequestMapping("/")
public class PatientController {
    @Autowired
    private PatientService patientService;

    @GetMapping(value = "patient-types")
    public ResponseEntity<Iterable<String>> getPatientTypes() {
        return ResponseEntity.ok(Arrays.stream(PatientType.values()).map(PatientType::toString).toList());
    }

    @GetMapping(value = "patient-sexes")
    public ResponseEntity<Iterable<String>> getPatientsSexes() {
        return ResponseEntity.ok(Arrays.stream(PatientSex.values()).map(PatientSex::toString).toList());
    }

    @GetMapping("patients")
    @PreAuthorize("hasRole('MEDIC')")
    Iterable<PatientDTO> getPatients() {
        return PatientDTOBuilder.toPatientDTOList(patientService.getAllPatients());
    }

    @GetMapping("patients/search")
    @PreAuthorize("hasRole('MEDIC')")
    Iterable<PatientDTO> searchPatients(@RequestParam String searchTerm) {
        return PatientDTOBuilder.toPatientDTOList(patientService.searchPatientsByName(searchTerm));
    }

    @GetMapping("type")
    @PreAuthorize("hasRole('MEDIC')")
    Iterable<String> getPatientsTypes() {
        return patientService.findDistinctTypes();
    }

    @GetMapping("breed")
    @PreAuthorize("hasRole('MEDIC')")
    Iterable<String> getPatientBreeds() {
        return patientService.findDistinctBreeds();
    }

    @GetMapping("sex")
    @PreAuthorize("hasRole('MEDIC')")
    Iterable<String> getPatientSexes() {
        return patientService.findDistinctSexes();
    }

    @GetMapping("color")
    @PreAuthorize("hasRole('MEDIC')")
    Iterable<String> getPatientColors() {
        return patientService.findDistinctColors();
    }

    @PostMapping(value = "patients")
    @PreAuthorize("hasRole('MEDIC')")
    public ResponseEntity<PatientDTO> registerPatient(@RequestBody PatientDTO patientDTO) {
        try {
            Patient patient = patientService.registerPatient(patientDTO);
            return ResponseEntity.ok(PatientDTOBuilder.toPatientDTO(patient));
        } catch (ValidatorException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (RepositoryException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @GetMapping(value = "patients/{id}")
    @PreAuthorize("hasRole('MEDIC')")
    public ResponseEntity<PatientDTO> getPatient(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(PatientDTOBuilder.toPatientDTO(patientService.getPatientById(id)));
        } catch (RepositoryException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @GetMapping("patient-exists")
    @PreAuthorize("hasRole('MEDIC')")
    public ResponseEntity<PatientExistsResponse> checkIfPatientExists(
            @RequestParam(value = "patientName") String patientName,
            @RequestParam("patientType") String patientType,
            @RequestParam(value = "patientBreed") String patientBreed,
            @RequestParam("patientSex") String patientSex,
            @RequestParam(value = "ownerEmail") String ownerEmail) {
        try {
            Patient patient = patientService.checkIfPatientExists(
                    patientName, PatientType.valueOf(patientType.trim().toUpperCase()), patientBreed, PatientSex.valueOf(patientSex.trim().toUpperCase()), ownerEmail);
            Boolean exists = patient != null;
            if (Boolean.TRUE.equals(exists)) {
                return ResponseEntity.ok(new PatientExistsResponse(patient.getId(), exists));
            } else {
                return ResponseEntity.ok(new PatientExistsResponse(null, exists));
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    @GetMapping("patients/by-owner/{ownerId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Iterable<PatientDTO>> getPatientsByOwnerId(@PathVariable Integer ownerId) {
        try {
            return ResponseEntity.ok(PatientDTOBuilder.toPatientDTOList(patientService.getPatientsByOwnerId(ownerId)));
        } catch (RepositoryException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

}
