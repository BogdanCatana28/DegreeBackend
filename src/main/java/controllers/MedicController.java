package controllers;

import model.dto.MedicDTO;
import model.dto.builders.MedicDTOBuilder;
import persistence.exceptions.RepositoryException;
import services.MedicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@RestController
@CrossOrigin
@RequestMapping("/medics")
public class MedicController {

    @Autowired
    private MedicService medicService;

    @GetMapping()
    public ResponseEntity<Iterable<MedicDTO>> getAllMedicsForProcedure(@RequestParam(required = false) Integer procedureId) {

        if (procedureId != null) {
            try {
                return ResponseEntity.ok(MedicDTOBuilder.toMedicDTOList(medicService.getMedicsForProcedure(procedureId)));
            } catch (RepositoryException e) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
            }
        }
        return ResponseEntity.ok(MedicDTOBuilder.toMedicDTOList(medicService.getAllMedics()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicDTO> showMedicById(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok().body(MedicDTOBuilder.toMedicDTO(medicService.getMedicById(id)));
        } catch (RepositoryException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @GetMapping("/adminList")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Iterable<MedicDTO>> getAllMedicsForAdmin(@RequestParam(required = false) Integer procedureId) {

        if (procedureId != null) {
            try {
                return ResponseEntity.ok(MedicDTOBuilder.toMedicDTOList(medicService.getMedicsForProcedure(procedureId)));
            } catch (RepositoryException e) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
            }
        }
        return ResponseEntity.ok(MedicDTOBuilder.toMedicDTOList(medicService.getAllMedics()));
    }

    @GetMapping("/adminList/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MedicDTO> showMedicByIdForAdmin(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok().body(MedicDTOBuilder.toMedicDTO(medicService.getMedicById(id)));
        } catch (RepositoryException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }
}
