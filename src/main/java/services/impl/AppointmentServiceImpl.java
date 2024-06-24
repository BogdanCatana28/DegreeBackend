package services.impl;

import model.Appointment;
import model.Medic;
import model.Procedure;
import model.Shift;
import model.dto.AppointmentDTO;
import model.dto.AppointmentMedicDTO;
import model.dto.builders.AppointmentDTOBuilder;
import model.dto.builders.AppointmentMedicDTOBuilder;
import model.enums.MedicSpecialization;
import persistence.AppointmentRepository;
import persistence.exceptions.RepositoryException;
import services.*;
import services.utils.ServiceException;
import validation.AppointmentValidator;
import validation.DateValidator;
import validation.utils.ValidatorException;
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
        Appointment appointment = AppointmentDTOBuilder.fromAppointmentDTO(appointmentDTO);

        appointment.setMedic(medicService.getMedicById(appointmentDTO.getMedicId()));
        appointment.setProcedure(procedureService.getProcedureById(appointmentDTO.getProcedureId()));

        if (appointment.getMedic() == null || appointment.getProcedure() == null) {
            throw new ServiceException("Medic or appointment id null");
        }

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
    public Iterable<Appointment> getAppointmentsByProcedureId(Integer procedureId, LocalDateTime startDate, LocalDateTime endDate) throws RepositoryException {
        Procedure procedure = procedureService.getProcedureById(procedureId);
        List<MedicSpecialization> procedureSpecializations = procedure.getSpecializations();

        Set<Medic> medics = new HashSet<>();

        for (var spec : procedureSpecializations) {
            medics.addAll((Collection<? extends Medic>) medicService.getMedicsBySpecialization(spec));
        }

        ArrayList<Appointment> appointments = new ArrayList<>();

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

        Medic med = medicService.getMedicById(medicId);
        Iterable<Shift> shifts = shiftService.getShiftsForMedicForDay(med, date);
        Set<LocalTime> availableSlots = new TreeSet<>();

        for (var shift : shifts) {
            for (LocalDateTime time = shift.getStartTime(); time.isBefore(shift.getEndTime()); time = time.plusMinutes(10)) {
                if (isMedicAvailable(med.getId(), time, time.plusMinutes(duration), shift)) {
                    availableSlots.add(time.toLocalTime());
                }
            }
        }
        return availableSlots;
    }

    @Override
    public Map<LocalTime, Set<AppointmentMedicDTO>> getAvailableSlotsForProcedure(Integer procedureId, LocalDate date) throws RepositoryException {
        Procedure procedure = procedureService.getProcedureById(procedureId);
        List<MedicSpecialization> procedureSpecializations = procedure.getSpecializations();

        Set<Medic> medics = new HashSet<>();

        for (var spec : procedureSpecializations) {
            for (Medic medic : medicService.getMedicsBySpecialization(spec)) {
                medics.add(medic);
            }
        }

        int duration = procedure.getDuration();
        Map<LocalTime, Set<AppointmentMedicDTO>> availableSlots = new TreeMap<>();

        for (var med : medics) {
            Iterable<Shift> shifts = shiftService.getShiftsForMedicForDay(med, date);

            for (var shift : shifts) {
                for (LocalDateTime time = shift.getStartTime(); time.isBefore(shift.getEndTime()); time = time.plusMinutes(duration)) {
                    if (isMedicAvailable(med.getId(), time, time.plusMinutes(duration), shift)) {
                        if (availableSlots.containsKey(time.toLocalTime())) {
                            availableSlots.get(time.toLocalTime()).add(AppointmentMedicDTOBuilder.toAppointmentMedicDTO(med));
                        } else {
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

        Map<LocalDateTime, LocalDateTime> betweenAppointments = new HashMap<>();
        for (int i = 0; i < appointmentStarts.size() - 1; i++) {
            betweenAppointments.put(appointmentEnds.get(i), appointmentStarts.get(i + 1));
        }
        betweenAppointments.put(shift.getStartTime(), appointmentStarts.get(0));
        betweenAppointments.put(appointmentEnds.get(appointmentEnds.size() - 1), shift.getEndTime());

        return betweenAppointments.entrySet().stream()
                .filter(entry -> (entry.getKey().isBefore(startTime) || entry.getKey().isEqual(startTime)) && (entry.getValue().isAfter(endTime) || entry.getValue().isEqual(endTime)))
                .toList().size() != 0;
    }

    public List<AppointmentDTO> getAppointmentsForWeekContainingDate(Integer medicId, LocalDate date) {
        List<Appointment> appointments = appointmentRepository.findByMedicId(medicId);
        List<AppointmentDTO> allAppointments = appointments.stream()
                .map(AppointmentDTOBuilder::toAppointmentDTO)
                .collect(Collectors.toList());

        allAppointments.sort(Comparator.comparing(AppointmentDTO::getDateReservation));

        LocalDate weekStartDate = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate weekEndDate = weekStartDate.plusDays(6);

        List<AppointmentDTO> appointmentsForWeek = allAppointments.stream()
                .filter(appointmentDTO ->
                        !appointmentDTO.getDateReservation().toLocalDate().isBefore(weekStartDate) &&
                                !appointmentDTO.getDateReservation().toLocalDate().isAfter(weekEndDate))
                .toList();

        return appointmentsForWeek;
    }

    @Override
    public void deleteAppointment(Appointment appointment) {
        appointmentRepository.delete(appointment);
    }

    @Override
    public Appointment getAppointmentById(Integer appointmentId) throws RepositoryException {
        return appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RepositoryException("Appointment not found"));
    }
}
