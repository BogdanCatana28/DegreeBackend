package fortech.team2.model;

import fortech.team2.model.enums.PatientSex;
import fortech.team2.model.enums.PatientType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    /** I think 'appointment_date' it's a better naming for the column
    */
    @Column(name = "dateAppointmentMade")
    private LocalDateTime dateAppointmentMade;

    /** I think 'reservation_date' it's a better naming for the column
     */
    @Column(name = "dateReservation")
    private LocalDateTime dateReservation;

    @ManyToOne
    private Procedure procedure;

    @ManyToOne
    /** You should also add @JoinColumn annotation which references the mapped column.
     * @JoinColumn(name = "medic_id")
     */
    private Medic medic;

    /** I think 'extra_notes' it's a better naming for the column
     */
    @Column(name = "extraNotes")
    private String extraNotes;

    // OWNER
    /** I think 'first_name_of_owner' or 'owner_first_name' it's a better naming for the column
     */
    @Column(name = "ownerFirstName")
    private String ownerFirstName;

    /** I think 'last_name_of_owner' or 'owner_last_name' it's a better naming for the column
     */
    @Column(name = "ownerLastName")
    private String ownerLastName;

    /** I think 'owner_address' it's a better naming for the column
     */
    @Column(name = "ownerAddress")
    private String ownerAddress;

    /** I think 'owner_email' it's a better naming for the column
     */
    @Column(name = "ownerEmail")
    private String ownerEmail;

    /** I think 'owner_phone' it's a better naming for the column
     */
    @Column(name = "ownerPhone")
    private String ownerPhone;

    // PATIENT
    /** I think 'patient_name' it's a better naming for the column
     */
    @Column(name = "patientName")
    private String patientName;

    /** I think 'patient_sex' it's a better naming for the column
     */
    @Column(name = "patientSex")
    private PatientSex patientSex;

    /** I think 'patient_type' it's a better naming for the column
     */
    @Column(name = "patientType")
    private PatientType patientType;

    /** I think 'patient_breed' it's a better naming for the column
     */
    @Column(name = "patientBreed")
    private String patientBreed;

    /** I think 'patient_colour' it's a better naming for the column
     */
    @Column(name = "patientColour")
    private String patientColour;

    @Column(name = "birth_date")
    private LocalDate patientBirthDate;
}
