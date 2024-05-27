package fortech.team2.services;

import fortech.team2.model.Appointment;
import fortech.team2.model.dto.AppointmentDTO;
import fortech.team2.model.dto.AppointmentMedicDTO;
import fortech.team2.persistence.exceptions.RepositoryException;
import fortech.team2.services.utils.ServiceException;
import fortech.team2.validation.utils.ValidatorException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface AppointmentService {

    Appointment addAppointment(AppointmentDTO appointmentDTO) throws ValidatorException, ServiceException, RepositoryException;

    Iterable<Appointment> getMedicAppointmentsForDay(Integer medicId, LocalDate day);

    Iterable<Appointment> getAppointmentsByProcedureId(Integer procedureId, LocalDateTime startDate, LocalDateTime endDate) throws RepositoryException;

    Set<LocalTime> getAvailableSlotsForMedic(Integer medicId, Integer procedureId, LocalDate date) throws RepositoryException;


    Map<LocalTime, Set<AppointmentMedicDTO>> getAvailableSlotsForProcedure(Integer procedureId, LocalDate date) throws RepositoryException;

    List<AppointmentDTO> getAppointmentsForWeekContainingDate(Integer medicId, LocalDate date) throws ServiceException;

    void deleteAppointment(Appointment appointment);
}
