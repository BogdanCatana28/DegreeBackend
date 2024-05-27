package fortech.team2.services.impl;

import fortech.team2.model.Medic;
import fortech.team2.model.Shift;
import fortech.team2.model.dto.ShiftDTO;
import fortech.team2.model.dto.builders.ShiftDTOBuilder;
import fortech.team2.persistence.ShiftRepository;
import fortech.team2.persistence.exceptions.RepositoryException;
import fortech.team2.services.DayOffService;
import fortech.team2.services.MedicService;
import fortech.team2.services.ShiftService;
import fortech.team2.services.utils.ServiceException;
import fortech.team2.validation.DateValidator;
import fortech.team2.validation.utils.ValidatorException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.stream.StreamSupport;

@Service
public class ShiftServiceImpl implements ShiftService {
    /**
     * You could also declare these autowired fields as 'final'
     */
    @Autowired
    private ShiftRepository shiftRepository;

    @Autowired
    private DayOffService dayOffService;

    @Autowired
    private MedicService medicService;


    @Override
    public Iterable<Shift> getAllShiftsBetween(LocalDateTime startTime, LocalDateTime endTime) {
        return shiftRepository.getShiftsByEndTimeIsAfterAndStartTimeIsBefore(startTime, endTime);
    }

    @Override
    public Iterable<Shift> getShiftsForMedicForDay(Medic medic, LocalDate day) {
        LocalDateTime date = LocalDateTime.of(day, LocalTime.of(0, 0, 0));
        return shiftRepository.getShiftsByMedicIdAndEndTimeIsAfterAndStartTimeIsBefore(medic.getId(), date, date.plusDays(1));
    }

    @Override
    public Shift addShift(ShiftDTO shiftDTO) throws RepositoryException, ServiceException, ValidatorException {
        Medic medic = medicService.getMedicById(shiftDTO.getMedicId());
        DateValidator.validateAgainstCurrentDate(shiftDTO.getStartTime());
        DateValidator.validateAgainstCurrentDate(shiftDTO.getEndTime());
        DateValidator.validateAfterEndOfNextWeek(shiftDTO.getStartTime().toLocalDate());
        if (dayOffService.isDayOff(medic.getId(), shiftDTO.getStartTime().toLocalDate()) || dayOffService.isDayOff(medic.getId(), shiftDTO.getEndTime().toLocalDate())) {
            /**
             * You should extract this message as a constant in a separated class named 'Constants' in 'utils' package , as public static final
             * ex: public static final String NO_AVAILABLE_MEDIC = "The medic is not available on this day!";
             * throw new RepositoryException(NO_AVAILABLE_MEDIC);
             */
            throw new ServiceException("The medic is not available on this day!");
        }
        /**
         * See the example above about String constants
         */
        if (getShiftsForMedicBetweenTimes(medic, shiftDTO.getStartTime(), shiftDTO.getEndTime()).spliterator().getExactSizeIfKnown() > 0) {
            throw new ServiceException("The medic already has a shift during these times!");
        }
        Shift shift = ShiftDTOBuilder.fromShiftDTO(shiftDTO);
        shift.setMedic(medic);
        /**
         * You should do the save into a try-catch block because of the errors that can appear, and not return directly
         */
        return shiftRepository.save(shift);
    }

    @Override
    public void removeShift(Integer id) throws RepositoryException, ValidatorException {
        Shift shift = shiftRepository.findById(id).orElseThrow(() -> new RepositoryException("Shift not found!"));
        DateValidator.validateAfterEndOfNextWeek(shift.getStartTime().toLocalDate());
        /**
         * You should do the delete into a try-catch block because of the errors that can appear
         */
        shiftRepository.deleteById(id);
    }

    @Override
    @Transactional(rollbackOn = {ServiceException.class, ValidatorException.class})
    public Shift updateShift(ShiftDTO shiftDTO) throws RepositoryException, ServiceException, ValidatorException {
        /**
         * Extract the next 4 lines into a separated method because the S (single responisibility) principle is violated
         * Call this method here atfter doing that
         */
        Shift shiftToUpdate = shiftRepository.findById(shiftDTO.getId()).orElseThrow(() -> new RepositoryException("Shift not found!"));
        shiftToUpdate.setStartTime(shiftDTO.getStartTime());
        shiftToUpdate.setEndTime(shiftDTO.getEndTime());
        shiftToUpdate.setMedic(medicService.getMedicById(shiftDTO.getMedicId()));
        /**
         * Extract the next 3 lines into a separated method because the S (single responisibility) principle is violated
         * Call this method here atfter doing that
         */
        DateValidator.validateAgainstCurrentDate(shiftToUpdate.getStartTime());
        DateValidator.validateAgainstCurrentDate(shiftToUpdate.getEndTime());
        DateValidator.validateAfterEndOfNextWeek(shiftDTO.getStartTime().toLocalDate());
        if (dayOffService.isDayOff(shiftDTO.getMedicId(), shiftDTO.getStartTime().toLocalDate()) || dayOffService.isDayOff(shiftDTO.getMedicId(), shiftDTO.getEndTime().toLocalDate())) {
            /**
             * See the example above about String constants
             */
            throw new ServiceException("The medic is not available on this day!");
        }
        shiftRepository.save(shiftToUpdate);
        if (getShiftsForMedicBetweenTimes(shiftToUpdate.getMedic(), shiftToUpdate.getStartTime(), shiftToUpdate.getEndTime()).spliterator().getExactSizeIfKnown() > 1) {
            throw new ServiceException("The medic already has a shift during these times!");
        }
        /**
         * You should do the save into a try-catch block because of the errors that can appear, and not return directly
         */
        return shiftRepository.save(shiftToUpdate);
    }

    @Override
    public void removeShiftsForMedicForDay(Medic medic, LocalDate freeDay) {
        Iterable<Shift> shifts = getShiftsForMedicForDay(medic, freeDay);
        /**
         * You should do the save into a try-catch block because of the errors that can appear
         */
        shiftRepository.deleteAll(shifts);
    }

    @Override
    public Iterable<Shift> getWeeklyShifts(LocalDate date) {
        Iterable<Shift> shifts = shiftRepository.findAll();

        LocalDate weekStartDate = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate weekEndDate = weekStartDate.plusDays(6);

        return StreamSupport.stream(shifts.spliterator(), false)
                .filter(shift ->
                        !shift.getStartTime().toLocalDate().isBefore(weekStartDate) &&
                                !shift.getEndTime().toLocalDate().isAfter(weekEndDate))
                .toList();
    }

    public Iterable<Shift> getShiftsForMedicBetweenTimes(Medic medic, LocalDateTime startTime, LocalDateTime endTime) {
        return shiftRepository.getShiftsByMedicIdAndEndTimeIsAfterAndStartTimeIsBefore(medic.getId(), startTime, endTime);
    }

}
