package services;

import model.DayOff;
import model.dto.DayOffDTO;
import persistence.exceptions.RepositoryException;
import services.utils.ServiceException;
import validation.utils.ValidatorException;

import java.time.LocalDate;

public interface DayOffService {
    DayOff takeDayOff(DayOffDTO dayOffDTO) throws ServiceException, RepositoryException, ValidatorException;

    Iterable<DayOff> getWeeklyDaysOff(LocalDate date) throws RepositoryException;

    boolean isDayOff(Integer medicId, LocalDate date);

    void deleteDayOff(Integer id) throws RepositoryException, ValidatorException;
}
