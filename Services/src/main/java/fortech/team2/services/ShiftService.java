package fortech.team2.services;

import fortech.team2.model.Medic;
import fortech.team2.model.Shift;
import fortech.team2.model.dto.ShiftDTO;
import fortech.team2.persistence.exceptions.RepositoryException;
import fortech.team2.services.utils.ServiceException;
import fortech.team2.validation.utils.ValidatorException;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface ShiftService {
    Iterable<Shift> getAllShiftsBetween(LocalDateTime startTime, LocalDateTime endTime);

    Iterable<Shift> getShiftsForMedicForDay(Medic medic, LocalDate day);

    Shift addShift(ShiftDTO shiftDTO) throws RepositoryException, ServiceException, ValidatorException;

    void removeShift(Integer id) throws RepositoryException, ValidatorException;

    Shift updateShift(ShiftDTO shift) throws RepositoryException, ServiceException, ValidatorException;

    void removeShiftsForMedicForDay(Medic medic, LocalDate freeDay);

    Iterable<Shift> getWeeklyShifts(LocalDate date);
}
