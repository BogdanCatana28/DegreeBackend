package fortech.team2.services;

import fortech.team2.model.DayOff;
import fortech.team2.model.dto.DayOffDTO;
import fortech.team2.persistence.exceptions.RepositoryException;
import fortech.team2.services.utils.ServiceException;
import fortech.team2.validation.utils.ValidatorException;

import java.time.LocalDate;

public interface DayOffService {
    DayOff takeDayOff(DayOffDTO dayOffDTO) throws ServiceException, RepositoryException, ValidatorException;

    Iterable<DayOff> getWeeklyDaysOff(LocalDate date) throws RepositoryException;

    boolean isDayOff(Integer medicId, LocalDate date);

    void deleteDayOff(Integer id) throws RepositoryException, ValidatorException;
}
