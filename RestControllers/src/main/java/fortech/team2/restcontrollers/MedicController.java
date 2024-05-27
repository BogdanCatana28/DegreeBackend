package fortech.team2.restcontrollers;

import fortech.team2.model.dto.MedicDTO;
import fortech.team2.model.dto.builders.MedicDTOBuilder;
import fortech.team2.persistence.exceptions.RepositoryException;
import fortech.team2.services.MedicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@RestController
@CrossOrigin
/**
 * You have to add 'medics' here in url and eliminate it from the below mappings
 */
@RequestMapping("/")
public class MedicController {
    /**
     * You could also declare this autowired field as 'final'
     */
    @Autowired
    private MedicService medicService;

    /**
     * Remove 'medics' from url (see first comment)
     */
    @GetMapping("medics")
    public ResponseEntity<Iterable<MedicDTO>> getAllMedicsForProcedure(@RequestParam(required = false) Integer procedureId) {
        /**
         * Move this verification in service method
         * A controller only call service methods
         */
        if (procedureId != null) {
            try {
                return ResponseEntity.ok(MedicDTOBuilder.toMedicDTOList(medicService.getMedicsForProcedure(procedureId)));
            } catch (RepositoryException e) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
            }
        }
        return ResponseEntity.ok(MedicDTOBuilder.toMedicDTOList(medicService.getAllMedics()));
    }

    /**
     * Remove 'medics' from url (see first comment)
     */
    @GetMapping("/medics/{id}")
    public ResponseEntity<MedicDTO> showMedicById(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok().body(MedicDTOBuilder.toMedicDTO(medicService.getMedicById(id)));
        } catch (RepositoryException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    /**
     * Remove 'medics' from url (see first comment)
     */
    @GetMapping("medics/adminList")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Iterable<MedicDTO>> getAllMedicsForAdmin(@RequestParam(required = false) Integer procedureId) {
        /**
         * Move this verification in service method
         * A controller only call service methods
         */
        if (procedureId != null) {
            try {
                return ResponseEntity.ok(MedicDTOBuilder.toMedicDTOList(medicService.getMedicsForProcedure(procedureId)));
            } catch (RepositoryException e) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
            }
        }
        return ResponseEntity.ok(MedicDTOBuilder.toMedicDTOList(medicService.getAllMedics()));
    }

    /**
     * Remove 'medics' from url (see first comment)
     */
    @GetMapping("/medics/adminList/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MedicDTO> showMedicByIdForAdmin(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok().body(MedicDTOBuilder.toMedicDTO(medicService.getMedicById(id)));
        } catch (RepositoryException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }
}
