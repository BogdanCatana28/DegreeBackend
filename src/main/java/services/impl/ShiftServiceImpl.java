package services.impl;

import model.Medic;
import model.Shift;
import model.dto.ShiftDTO;
import model.dto.builders.ShiftDTOBuilder;
import persistence.ShiftRepository;
import persistence.exceptions.RepositoryException;
import services.DayOffService;
import services.MedicService;
import services.ShiftService;
import services.utils.ServiceException;
import validation.DateValidator;
import validation.utils.ValidatorException;
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
        if (dayOffService.isDayOff(medic.getId(), shiftDTO.getStartTime().toLocalDate()) || dayOffService.isDayOff(medic.getId(), shiftDTO.getEndTime().toLocalDate())) {
            throw new ServiceException("The medic is not available on this day!");
        }
        if (getShiftsForMedicBetweenTimes(medic, shiftDTO.getStartTime(), shiftDTO.getEndTime()).spliterator().getExactSizeIfKnown() > 0) {
            throw new ServiceException("The medic already has a shift during these times!");
        }
        Shift shift = ShiftDTOBuilder.fromShiftDTO(shiftDTO);
        shift.setMedic(medic);

        return shiftRepository.save(shift);
    }

    @Override
    public void removeShift(Integer id) throws RepositoryException, ValidatorException {
        Shift shift = shiftRepository.findById(id).orElseThrow(() -> new RepositoryException("Shift not found!"));

        shiftRepository.deleteById(id);
    }

    @Override
    @Transactional(rollbackOn = {ServiceException.class, ValidatorException.class})
    public Shift updateShift(ShiftDTO shiftDTO) throws RepositoryException, ServiceException, ValidatorException {

        Shift shiftToUpdate = shiftRepository.findById(shiftDTO.getId()).orElseThrow(() -> new RepositoryException("Shift not found!"));
        shiftToUpdate.setStartTime(shiftDTO.getStartTime());
        shiftToUpdate.setEndTime(shiftDTO.getEndTime());
        shiftToUpdate.setMedic(medicService.getMedicById(shiftDTO.getMedicId()));

        DateValidator.validateAgainstCurrentDate(shiftToUpdate.getStartTime());
        DateValidator.validateAgainstCurrentDate(shiftToUpdate.getEndTime());
        if (dayOffService.isDayOff(shiftDTO.getMedicId(), shiftDTO.getStartTime().toLocalDate()) || dayOffService.isDayOff(shiftDTO.getMedicId(), shiftDTO.getEndTime().toLocalDate())) {

            throw new ServiceException("The medic is not available on this day!");
        }
        shiftRepository.save(shiftToUpdate);
        if (getShiftsForMedicBetweenTimes(shiftToUpdate.getMedic(), shiftToUpdate.getStartTime(), shiftToUpdate.getEndTime()).spliterator().getExactSizeIfKnown() > 1) {
            throw new ServiceException("The medic already has a shift during these times!");
        }

        return shiftRepository.save(shiftToUpdate);
    }

    @Override
    public void removeShiftsForMedicForDay(Medic medic, LocalDate freeDay) {
        Iterable<Shift> shifts = getShiftsForMedicForDay(medic, freeDay);

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
