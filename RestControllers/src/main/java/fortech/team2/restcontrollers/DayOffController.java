package fortech.team2.restcontrollers;

import fortech.team2.model.dto.DayOffDTO;
import fortech.team2.model.dto.builders.DayOffDTOBuilder;
import fortech.team2.persistence.exceptions.RepositoryException;
import fortech.team2.services.DayOffService;
import fortech.team2.services.utils.ServiceException;
import fortech.team2.validation.utils.ValidatorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

@RestController
@CrossOrigin
@RequestMapping("/days-off")
public class DayOffController {
    /**
     * You could also declare this autowired field as 'final'
     */
    @Autowired
    private DayOffService dayOffService;

    @PostMapping
    public ResponseEntity<DayOffDTO> takeDayOff(@RequestBody DayOffDTO dayOffDTO) {
        try {
            return ResponseEntity.ok(DayOffDTOBuilder.toDayOffDTO(dayOffService.takeDayOff(dayOffDTO)));
        } catch (ServiceException | ValidatorException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (RepositoryException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @GetMapping("/weekly")
    public ResponseEntity<Iterable<DayOffDTO>> getWeeklyDaysOffForMedic(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try {
            return ResponseEntity.ok(DayOffDTOBuilder.toDayOffDTOList(dayOffService.getWeeklyDaysOff(date)));
        } catch (RepositoryException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @DeleteMapping("/{id}")
    public void deleteDayOff(@PathVariable Integer id) {
        try {
            dayOffService.deleteDayOff(id);
        } catch (RepositoryException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (ValidatorException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

}
