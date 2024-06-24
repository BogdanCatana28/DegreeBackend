package services.impl;

import model.Appointment;
import model.DayOff;
import model.Medic;
import model.dto.DayOffDTO;
import persistence.DayOffRepository;
import persistence.exceptions.RepositoryException;
import services.*;
import services.utils.ServiceException;
import validation.DateValidator;
import validation.utils.ValidatorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.stream.StreamSupport;

@Service
public class DayOffServiceImpl implements DayOffService {

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

        if (daysOffUsed >= medic.getDaysOff()) {

            throw new ServiceException("You have already used all your days off!");
        }
        if (dayOffRepository.existsByMedicIdAndFreeDay(medicId, freeDay)) {

            throw new ServiceException("You have already taken a day off on this day!");
        }

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

        dayOffRepository.deleteById(id);
    }
}
