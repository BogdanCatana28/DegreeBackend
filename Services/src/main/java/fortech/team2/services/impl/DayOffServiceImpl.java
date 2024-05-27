package fortech.team2.services.impl;

import fortech.team2.model.Appointment;
import fortech.team2.model.DayOff;
import fortech.team2.model.Medic;
import fortech.team2.model.dto.DayOffDTO;
import fortech.team2.persistence.DayOffRepository;
import fortech.team2.persistence.exceptions.RepositoryException;
import fortech.team2.services.*;
import fortech.team2.services.utils.ServiceException;
import fortech.team2.validation.DateValidator;
import fortech.team2.validation.utils.ValidatorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.stream.StreamSupport;

@Service
public class DayOffServiceImpl implements DayOffService {

    /**
     * You could also declare these autowired fields as 'final'
     */
    @Autowired
    private DayOffRepository dayOffRepository;

    @Autowired
    private MedicService medicService;

    @Autowired
    private ShiftService shiftService;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private EmailService emailService;

    @Override
    public DayOff takeDayOff(DayOffDTO dayOffDTO) throws ServiceException, RepositoryException, ValidatorException {
        LocalDate freeDay = dayOffDTO.getFreeDay();
        Integer medicId = dayOffDTO.getMedicId();
        Medic medic = medicService.getMedicById(medicId);
        long daysOffUsed = dayOffRepository.getDaysOffByMedicId(medicId).spliterator().getExactSizeIfKnown();
        DateValidator.validateAgainstCurrentDate(freeDay);
        /**
         * Do this validations( lines 53 and 59) before the call of validateAgainstCurrentDate from 48 line
         * because is redundant to validate something else if the medic already used all his days off
         */
        if (daysOffUsed >= medic.getDaysOff()) {
            /**
             * You should extract this message as a constant in a separated class named 'Constants' in 'utils' package , as public static final
             */
            throw new ServiceException("You have already used all your days off!");
        }
        if (dayOffRepository.existsByMedicIdAndFreeDay(medicId, freeDay)) {
            /**
             * You should extract this message as a constant in a separated class named 'Constants' in 'utils' package , as public static final
             */
            throw new ServiceException("You have already taken a day off on this day!");
        }

        /**
         * Move the instantion of 'dayOff' object at line 81.
         * If an error will occur in 75-81 lines, this allocation was made in vain
         */
        DayOff dayOff = DayOff.builder()
                .medic(medic)
                .freeDay(freeDay)
                .build();

        shiftService.removeShiftsForMedicForDay(medic, freeDay);

        Iterable<Appointment> appointments = appointmentService.getMedicAppointmentsForDay(medic.getId(), freeDay);
        appointments.forEach(appointment -> {
            appointmentService.deleteAppointment(appointment);
            emailService.sendAppointmentCancellation(appointment);
        });

        /**
         * You should do the save into a try-catch block because of the errors that can appear
         * Do not save it in 'dayOff' object
         */
        dayOff = dayOffRepository.save(dayOff);
        emailService.sendDayOffConfirmation(dayOff);
        return dayOff;
    }

    @Override
    public Iterable<DayOff> getWeeklyDaysOff(LocalDate date) throws RepositoryException {
        Iterable<DayOff> daysOff = dayOffRepository.findAll();

        LocalDate weekStartDate = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate weekEndDate = weekStartDate.plusDays(6);

        return StreamSupport.stream(daysOff.spliterator(), false)
                .filter(dayOff ->
                        !dayOff.getFreeDay().isBefore(weekStartDate) &&
                                !dayOff.getFreeDay().isAfter(weekEndDate))
                .toList();
    }

    @Override
    public boolean isDayOff(Integer medicId, LocalDate date) {
        return dayOffRepository.existsByMedicIdAndFreeDay(medicId, date);
    }

    @Override
    public void deleteDayOff(Integer id) throws RepositoryException, ValidatorException {
        DayOff dayOff = dayOffRepository.findById(id).orElseThrow(() -> new RepositoryException("Day off not found!"));
        DateValidator.validateAfterEndOfNextWeek(dayOff.getFreeDay());
        /**
         * You should do the delete into a try-catch block because of the errors that can appear
         */
        dayOffRepository.deleteById(id);
    }
}
