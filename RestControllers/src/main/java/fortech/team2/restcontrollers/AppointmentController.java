package fortech.team2.restcontrollers;

import fortech.team2.model.Appointment;
import fortech.team2.model.dto.AppointmentDTO;
import fortech.team2.model.dto.builders.AppointmentDTOBuilder;
import fortech.team2.persistence.exceptions.RepositoryException;
import fortech.team2.services.AppointmentService;
import fortech.team2.services.utils.ServiceException;
import fortech.team2.validation.utils.ValidatorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDate;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/appointments")
public class AppointmentController {

    /**
     * You could also declare this autowired field as 'final'
     */
    @Autowired
    private AppointmentService appointmentService;

    @PostMapping()
    public ResponseEntity<AppointmentDTO> addAppointment(@RequestBody AppointmentDTO appointmentDTO) {
        try {
            Appointment newAppointment = appointmentService.addAppointment(appointmentDTO);
            return ResponseEntity.ok().body(AppointmentDTOBuilder.toAppointmentDTO(newAppointment));
        } catch (ValidatorException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (RepositoryException | ServiceException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }

    }

    @GetMapping
    public ResponseEntity<?> getAvailableSlots(@RequestParam(value = "medicId", required = false) Integer medicId,
                                               @RequestParam(value = "procedureId") Integer procedureId,
                                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try {
            if (medicId == null) {
                return ResponseEntity.ok(appointmentService.getAvailableSlotsForProcedure(procedureId, date));
            } else {
                return ResponseEntity.ok(appointmentService.getAvailableSlotsForMedic(medicId, procedureId, date));
            }
        } catch (RepositoryException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }

    }

    @GetMapping("/weekly")
    public ResponseEntity<Iterable<AppointmentDTO>> getAppointmentsForWeekContainingDate(
            @RequestParam("medicId") Integer medicId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try {
            List<AppointmentDTO> appointmentsForWeek = appointmentService.getAppointmentsForWeekContainingDate(medicId, date);
            return ResponseEntity.ok(appointmentsForWeek);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

}
