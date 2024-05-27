
package fortech.team2.model.dto.builders;

import fortech.team2.model.Appointment;
import fortech.team2.model.dto.AppointmentDTO;
import fortech.team2.model.enums.PatientSex;
import fortech.team2.model.enums.PatientType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;

public class AppointmentDTOBuilder {
    /**
     * Use @NoArgsContructor instead of this
     */
    private AppointmentDTOBuilder() {
    }

    /**
     * Simply name 'toEntity'
     */
    public static Appointment fromAppointmentDTO(AppointmentDTO appointmentDTO) {
        return Appointment.builder()
                .dateAppointmentMade(LocalDateTime.now())
                .dateReservation(appointmentDTO.getDateReservation())
                .extraNotes(appointmentDTO.getExtraNotes())
                .ownerFirstName(appointmentDTO.getOwnerFirstName())
                .ownerLastName(appointmentDTO.getOwnerLastName())
                .ownerAddress(appointmentDTO.getOwnerAddress())
                .ownerPhone(appointmentDTO.getOwnerPhone())
                .ownerEmail(appointmentDTO.getOwnerEmail())
                .patientName(appointmentDTO.getPatientName())
                .patientSex(PatientSex.valueOf(appointmentDTO.getPatientSex().trim().toUpperCase()))
                .patientType(PatientType.valueOf(appointmentDTO.getPatientType().trim().toUpperCase()))
                .patientBreed(appointmentDTO.getPatientBreed())
                .patientColour(appointmentDTO.getPatientColour())
                .patientBirthDate(
                        appointmentDTO.getPatientAge() != null ?
                                LocalDate.now().minusMonths(appointmentDTO.getPatientAge()) :
                                null
                )
                .build();
    }

    /**
     * Simply name 'toDTO'
     */
    public static AppointmentDTO toAppointmentDTO(Appointment appointment) {
        return AppointmentDTO.builder()
                .id(appointment.getId())
                .medicId(appointment.getMedic().getId())
                .procedureId(appointment.getProcedure().getId())
                .dateReservation(appointment.getDateReservation())
                .ownerFirstName(appointment.getOwnerFirstName())
                .ownerLastName(appointment.getOwnerLastName())
                .ownerAddress(appointment.getOwnerAddress())
                .ownerPhone(appointment.getOwnerPhone())
                .ownerEmail(appointment.getOwnerEmail())
                .patientName(appointment.getOwnerFirstName())
                .patientName(appointment.getPatientName())
                .patientSex(appointment.getPatientSex().toString())
                .patientType(appointment.getPatientType().toString())
                .patientBreed(appointment.getPatientBreed())
                .patientColour(appointment.getPatientColour())
                .extraNotes(appointment.getExtraNotes())
                .patientAge(
                        appointment.getPatientBirthDate() != null ?
                                Period.between(appointment.getPatientBirthDate(), LocalDate.now()).getYears() * 12 +
                                        Period.between(appointment.getPatientBirthDate(), LocalDate.now()).getMonths() :
                                null
                )
                .build();
    }

    public static Iterable<AppointmentDTO> toAppointmentDTOList(Iterable<Appointment> appointments) {
        ArrayList<AppointmentDTO> appointmentDTOS = new ArrayList<>();
        appointments.forEach(medic -> appointmentDTOS.add(toAppointmentDTO(medic)));

        return appointmentDTOS;
    }
}
