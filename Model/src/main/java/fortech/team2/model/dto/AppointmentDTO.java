package fortech.team2.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentDTO {
    private Integer id;
    private LocalDateTime dateReservation;
    private Integer procedureId;
    private Integer medicId;
    private String extraNotes; // any extra notes a customer might have

    // OWNER
    private String ownerFirstName;
    private String ownerLastName;
    private String ownerAddress;
    private String ownerEmail;
    private String ownerPhone;

    // PATIENT
    private String patientName;
    private String patientSex;
    private String patientType;
    private String patientBreed;
    private String patientColour;
    private Integer patientAge;
}
