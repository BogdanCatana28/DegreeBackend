package fortech.team2.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "consultations")
public class Consultation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "consultation_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "patient_id", referencedColumnName = "patient_id")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private User owner;

    @Column(name = "main_concern")
    private String mainConcern;

    @Column(name = "history_of_concern")
    private String historyOfConcern;

    @Column(name = "diagnostic")
    private String diagnostic;

    @Column(name = "treatment")
    private String treatment;

    @Column(name = "extra_notes")
    private String extraNotes;

    @Column(name = "creation_date")
    private LocalDate creationDate;
}

