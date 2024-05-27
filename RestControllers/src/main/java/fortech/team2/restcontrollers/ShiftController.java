package fortech.team2.restcontrollers;

import fortech.team2.model.dto.ShiftDTO;
import fortech.team2.model.dto.builders.ShiftDTOBuilder;
import fortech.team2.persistence.exceptions.RepositoryException;
import fortech.team2.services.ShiftService;
import fortech.team2.services.utils.ServiceException;
import fortech.team2.validation.utils.ValidatorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@CrossOrigin
@RequestMapping("/shifts")
public class ShiftController {
    @Autowired
    private ShiftService shiftService;

    @GetMapping()
    public ResponseEntity<Iterable<ShiftDTO>> getShiftsBetween(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                               LocalDateTime startTime,
                                                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                               LocalDateTime endTime) {

        return ResponseEntity.ok(ShiftDTOBuilder.toShiftDTOList(shiftService.getAllShiftsBetween(startTime, endTime)));
    }

    @GetMapping("/weekly")
    public ResponseEntity<Iterable<ShiftDTO>> getWeeklyShifts(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try {
            return ResponseEntity.ok(ShiftDTOBuilder.toShiftDTOList(shiftService.getWeeklyShifts(date)));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    @PostMapping
    public ResponseEntity<ShiftDTO> addShift(@RequestBody ShiftDTO shiftDTO) {
        try {
            return ResponseEntity.ok(ShiftDTOBuilder.toShiftDTO(shiftService.addShift(shiftDTO)));
        } catch (RepositoryException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (ServiceException | ValidatorException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @PutMapping
    public ResponseEntity<ShiftDTO> updateShift(@RequestBody ShiftDTO shiftDTO) {
        try {
            return ResponseEntity.ok(ShiftDTOBuilder.toShiftDTO(shiftService.updateShift(shiftDTO)));
        } catch (RepositoryException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (ServiceException | ValidatorException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @DeleteMapping("/{id}")
    public void deleteShift(@PathVariable Integer id) {
        try {
            shiftService.removeShift(id);
        } catch (RepositoryException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (ValidatorException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }
}
