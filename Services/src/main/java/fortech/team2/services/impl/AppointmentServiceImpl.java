package fortech.team2.services.impl;

import fortech.team2.model.Appointment;
import fortech.team2.model.Medic;
import fortech.team2.model.Procedure;
import fortech.team2.model.Shift;
import fortech.team2.model.dto.AppointmentDTO;
import fortech.team2.model.dto.AppointmentMedicDTO;
import fortech.team2.model.dto.builders.AppointmentDTOBuilder;
import fortech.team2.model.dto.builders.AppointmentMedicDTOBuilder;
import fortech.team2.model.enums.MedicSpecialization;
import fortech.team2.persistence.AppointmentRepository;
import fortech.team2.persistence.exceptions.RepositoryException;
import fortech.team2.services.*;
import fortech.team2.services.utils.ServiceException;
import fortech.team2.validation.AppointmentValidator;
import fortech.team2.validation.DateValidator;
import fortech.team2.validation.utils.ValidatorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class AppointmentServiceImpl implements AppointmentService {
    /**
     * You could also declare these autowired fields as 'final'
     */
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private AppointmentValidator appointmentValidator;
    @Autowired
    private MedicService medicService;
    @Autowired
    private ProcedureService procedureService;
    @Autowired
    private EmailService emailService;

    @Autowired
    private ShiftService shiftService;

    @Override
    public Appointment addAppointment(AppointmentDTO appointmentDTO) throws ValidatorException, ServiceException, RepositoryException {
        appointmentValidator.validate(appointmentDTO);
        DateValidator.validateBeforeEndOfNextWeek(appointmentDTO.getDateReservation().toLocalDate());
        Appointment appointment = AppointmentDTOBuilder.fromAppointmentDTO(appointmentDTO);

        appointment.setMedic(medicService.getMedicById(appointmentDTO.getMedicId()));
        appointment.setProcedure(procedureService.getProcedureById(appointmentDTO.getProcedureId()));

        if (appointment.getMedic() == null || appointment.getProcedure() == null) {
            /**
             * You should extract this message as a constant in a separated class named 'Constants' in 'utils' package , as public static final
             */
            throw new ServiceException("Medic or appointment id null");
        }

        /**
         * You should do the save into a try-catch block because of the errors that can appear
         */
        Appointment savedAppointment = appointmentRepository.save(appointment);
        emailService.sendAppointmentConfirmation(appointment);

        return savedAppointment;
    }

    @Override
    public Iterable<Appointment> getMedicAppointmentsForDay(Integer medicId, LocalDate day) {
        LocalDateTime date = LocalDateTime.of(day, LocalTime.of(0, 0, 0));
        return appointmentRepository.getAppointmentByMedicIdAndDateReservationBetween(medicId, date, date.plusDays(1));

    }

    @Override
    /**
     * Do not leave these comments '//'.
     * The code should be clean so that there is no need for comments on each line
     */
    public Iterable<Appointment> getAppointmentsByProcedureId(Integer procedureId, LocalDateTime startDate, LocalDateTime endDate) throws RepositoryException {
        // all appointments with a medic with a specialization for procedureId

        // get procedure specializations
        /**
         * What will happen if there is no procedure returned from db? Take care of this case !
         */
        Procedure procedure = procedureService.getProcedureById(procedureId);
        List<MedicSpecialization> procedureSpecializations = procedure.getSpecializations();

        // get all medics that can perform prodcedureId
        Set<Medic> medics = new HashSet<>();
        /**
         * ABSOLUTELY NO !
         * IN JAVA WE DO NOT USE 'var' !!!
         */
        for (var spec : procedureSpecializations) {
            medics.addAll((Collection<? extends Medic>) medicService.getMedicsBySpecialization(spec));
        }

        // get all app with those medics within that time period
        ArrayList<Appointment> appointments = new ArrayList<>();
        /**
         * ABSOLUTELY NO !
         * IN JAVA WE DO NOT USE 'var' !!!
         */
        for (var med : medics) {
            appointments.addAll((Collection<? extends Appointment>) appointmentRepository.getAppointmentByMedicIdAndDateReservationBetween(
                    med.getId(), startDate, endDate));

        }

        return appointments;
    }


    @Override
    public Set<LocalTime> getAvailableSlotsForMedic(Integer medicId, Integer procedureId, LocalDate date) throws RepositoryException {
        Procedure procedure = procedureService.getProcedureById(procedureId);
        int duration = procedure.getDuration();
        /**
         * What will happen if there is no medic returned from db? Take care of this case !
         */
        Medic med = medicService.getMedicById(medicId);
        Iterable<Shift> shifts = shiftService.getShiftsForMedicForDay(med, date);
        Set<LocalTime> availableSlots = new TreeSet<>();
        /**
         * ABSOLUTELY NO !
         * IN JAVA WE DO NOT USE 'var' !!!
         */
        for (var shift : shifts) {
            for (LocalDateTime time = shift.getStartTime(); time.isBefore(shift.getEndTime()); time = time.plusMinutes(10)) {
                /**
                 * do not leave console prints in code
                 */
                System.out.println("AppointmentServiceImpl.getAvailableSlotsForProcedure");
                System.out.println("AppointmentServiceImpl.getAvailableSlotsForProcedure");
                System.out.println("AppointmentServiceImpl.getAvailableSlotsForProcedure");
                System.out.println(time);
                if (isMedicAvailable(med.getId(), time, time.plusMinutes(duration), shift)) {
                    availableSlots.add(time.toLocalTime());
                }
            }
        }
        return availableSlots;
    }

    @Override
    public Map<LocalTime, Set<AppointmentMedicDTO>> getAvailableSlotsForProcedure(Integer procedureId, LocalDate date) throws RepositoryException {
        /**
         * What will happen if there is no procedure returned from db? Take care of this case !
         */
        Procedure procedure = procedureService.getProcedureById(procedureId);
        /**
         * What will happen if there are no specializations returned from db? Take care of this case !
         */
        List<MedicSpecialization> procedureSpecializations = procedure.getSpecializations();
        // get all medics that can perform prodcedureId
        Set<Medic> medics = new HashSet<>();
        /**
         * OMG....
         * ABSOLUTELY NO !
         * IN JAVA WE DO NOT USE 'var' !!!
         */
        for (var spec : procedureSpecializations) {
            for (Medic medic : medicService.getMedicsBySpecialization(spec)) {
                medics.add(medic);
            }
        }

        int duration = procedure.getDuration();
        Map<LocalTime, Set<AppointmentMedicDTO>> availableSlots = new TreeMap<>();
        /**
         * ABSOLUTELY NO !
         * IN JAVA WE DO NOT USE 'var' !!!
         */
        for (var med : medics) {
            Iterable<Shift> shifts = shiftService.getShiftsForMedicForDay(med, date);
            /**
             * ABSOLUTELY NO !
             * IN JAVA WE DO NOT USE 'var' !!!
             */
            for (var shift : shifts) {
                for (LocalDateTime time = shift.getStartTime(); time.isBefore(shift.getEndTime()); time = time.plusMinutes(duration)) {
                    if (isMedicAvailable(med.getId(), time, time.plusMinutes(duration), shift)) {
                        if (availableSlots.containsKey(time.toLocalTime())) {
                            availableSlots.get(time.toLocalTime()).add(AppointmentMedicDTOBuilder.toAppointmentMedicDTO(med));
                        } else {
                            /**
                             * Extract the next 3 lines into a separated method because the S (single responisibility) principle is violated
                             * Call this method here atfter doing that
                             */
                            Set<AppointmentMedicDTO> medicsSet = new HashSet<>();
                            medicsSet.add(AppointmentMedicDTOBuilder.toAppointmentMedicDTO(med));
                            availableSlots.put(time.toLocalTime(), medicsSet);
                        }
                    }
                }
            }

        }
        return availableSlots;
    }

    /**
     * Absolutely agree with your comment ;)
     */
    //not the cleanest code, but the quickest hotfix in the west

    /**
     * 'medId' is not a well named variable
     */
    private boolean isMedicAvailable(Integer medId, LocalDateTime startTime, LocalDateTime endTime, Shift shift) {
        Iterable<Appointment> appointments = appointmentRepository.getAppointmentByMedicIdAndDateReservationBetween(medId, startTime.toLocalDate().atStartOfDay(), endTime.toLocalDate().plusDays(1).atStartOfDay());

        if (appointments.spliterator().getExactSizeIfKnown() == 0)
            return true;

        List<LocalDateTime> appointmentStarts = StreamSupport.stream(appointments.spliterator(), false)
                .map(Appointment::getDateReservation)
                .toList();
        List<LocalDateTime> appointmentEnds = StreamSupport.stream(appointments.spliterator(), false)
                .map(appointment -> appointment.getDateReservation().plusMinutes(appointment.getProcedure().getDuration()))
                .toList();

        /**
         * I will extract these lines into a separated method.. This one is responsible for too many
         */
        Map<LocalDateTime, LocalDateTime> betweenAppointments = new HashMap<>();
        for (int i = 0; i < appointmentStarts.size() - 1; i++) {
            betweenAppointments.put(appointmentEnds.get(i), appointmentStarts.get(i + 1));
        }
        betweenAppointments.put(shift.getStartTime(), appointmentStarts.get(0));
        betweenAppointments.put(appointmentEnds.get(appointmentEnds.size() - 1), shift.getEndTime());
        /**
         * do not leave console prints in code
         */
        System.out.println(betweenAppointments);
        return betweenAppointments.entrySet().stream()
                .filter(entry -> (entry.getKey().isBefore(startTime) || entry.getKey().isEqual(startTime)) && (entry.getValue().isAfter(endTime) || entry.getValue().isEqual(endTime)))
                .toList().size() != 0;
    }

    public List<AppointmentDTO> getAppointmentsForWeekContainingDate(Integer medicId, LocalDate date) {
        /**
         * What will happen if there are no appointments returned from db? Take care of this case !
         */
        List<Appointment> appointments = appointmentRepository.findByMedicId(medicId);
        List<AppointmentDTO> allAppointments = appointments.stream()
                .map(AppointmentDTOBuilder::toAppointmentDTO)
                .collect(Collectors.toList());

        /**
         * You could make this sort inside stream
         *  .sorted(Comparator.comparing(AppointmentDTO::getDateReservation))
         */
        allAppointments.sort(Comparator.comparing(AppointmentDTO::getDateReservation));

        LocalDate weekStartDate = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate weekEndDate = weekStartDate.plusDays(6);

        /**
         * Extract the next lines into a separated method because the S (single responisibility) principle is violated
         * Call this method here atfter doing that
         */
        List<AppointmentDTO> appointmentsForWeek = allAppointments.stream()
                .filter(appointmentDTO ->
                        !appointmentDTO.getDateReservation().toLocalDate().isBefore(weekStartDate) &&
                                !appointmentDTO.getDateReservation().toLocalDate().isAfter(weekEndDate))
                .toList();

        return appointmentsForWeek;
    }

    @Override
    public void deleteAppointment(Appointment appointment) {
        /**
         * You should do the delete into a try-catch block because of the errors that can appear
         */
        appointmentRepository.delete(appointment);
    }


}
