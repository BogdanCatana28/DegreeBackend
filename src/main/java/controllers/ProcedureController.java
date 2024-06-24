package controllers;

import model.Procedure;
import persistence.exceptions.RepositoryException;
import services.ProcedureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@CrossOrigin

@RequestMapping("/procedures")
public class ProcedureController {

    @Autowired
    private ProcedureService procedureService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Procedure> addProcedure(@RequestBody Procedure procedure) {
        return ResponseEntity.ok().body(procedureService.addProcedure(procedure));
    }

    @GetMapping
    List<Procedure> showProcedures() {
        return procedureService.showProcedures();
    }

    @GetMapping("/{id}")
    ResponseEntity<Procedure> showProcedureById(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok().body(procedureService.getProcedureById(id));
        } catch (RepositoryException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<Procedure> updateProcedure(@RequestBody Procedure newProcedure, @PathVariable Integer id) {
        try {
            return ResponseEntity.ok().body(procedureService.updateProcedure(newProcedure, id));
        } catch (RepositoryException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteProcedure(@PathVariable Integer id) {
        procedureService.delete(id);
    }
}
