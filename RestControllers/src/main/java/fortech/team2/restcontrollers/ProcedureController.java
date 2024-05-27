package fortech.team2.restcontrollers;

import fortech.team2.model.Procedure;
import fortech.team2.persistence.exceptions.RepositoryException;
import fortech.team2.services.ProcedureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@CrossOrigin
/**
 * You have to add 'procedure' here in url and eliminate it from the below mappings
 */
@RequestMapping
public class ProcedureController {

    @Autowired
    private ProcedureService procedureService;

    /**
     * Remove 'procedures' from url (see first comment)
     */
    @PostMapping("/procedures")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Procedure> addProcedure(@RequestBody Procedure procedure) {
        return ResponseEntity.ok().body(procedureService.addProcedure(procedure));
    }

    /**
     * Remove 'procedures' from url (see first comment)
     */
    @GetMapping("/procedures")
    List<Procedure> showProcedures() {
        return procedureService.showProcedures();
    }

    /**
     * Remove 'procedures' from url (see first comment)
     */
    @GetMapping("/procedures/{id}")
    ResponseEntity<Procedure> showProcedureById(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok().body(procedureService.getProcedureById(id));
        } catch (RepositoryException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    /**
     * Remove 'procedures' from url (see first comment)
     */
    @PutMapping("/procedures/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<Procedure> updateProcedure(@RequestBody Procedure newProcedure, @PathVariable Integer id) {
        try {
            return ResponseEntity.ok().body(procedureService.updateProcedure(newProcedure, id));
        } catch (RepositoryException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    /**
     * Remove 'procedures' from url (see first comment)
     */
    @DeleteMapping("/procedures/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteProcedure(@PathVariable Integer id) {
        procedureService.delete(id);
    }
}
