package model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.enums.PatientSex;
import model.enums.PatientType;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "appointments")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appointment_id")
    private Integer id;

    @Column(name = "dateAppointmentMade")
    private LocalDateTime dateAppointmentMade;

    @Column(name = "dateReservation")
    private LocalDateTime dateReservation;

    @ManyToOne
    private Procedure procedure;

    @ManyToOne
    private Medic medic;

    @Column(name = "extraNotes")
    private String extraNotes;

    @Column(name = "ownerFirstName")
    private String ownerFirstName;

    @Column(name = "ownerLastName")
    private String ownerLastName;

    @Column(name = "ownerAddress")
    private String ownerAddress;

    @Column(name = "ownerEmail")
    private String ownerEmail;

    @Column(name = "ownerPhone")
    private String ownerPhone;

    @Column(name = "patientName")
    private String patientName;

    @Column(name = "patientSex")
    private PatientSex patientSex;

    @Column(name = "patientType")
    private PatientType patientType;

    @Column(name = "patientBreed")
    private String patientBreed;

    @Column(name = "patientColour")
    private String patientColour;

    @Column(name = "birth_date")
    private LocalDate patientBirthDate;
}
