package services;

import model.Medic;
import model.Shift;
import model.dto.ShiftDTO;
import persistence.exceptions.RepositoryException;
import services.utils.ServiceException;
import validation.utils.ValidatorException;

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
