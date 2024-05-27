package fortech.team2.model;

import fortech.team2.model.enums.PatientSex;
import fortech.team2.model.enums.PatientType;
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
@Table(name = "patients")
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "patient_id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "sex")
    @Enumerated(EnumType.STRING)
    private PatientSex sex;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private PatientType type;

    @Column(name = "breed")
    private String breed;

    @Column(name = "color")
    private String color;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "weight")
    private Float weight;

    @Column(name = "medical_history")
    private String medicalHistory;

    @ManyToOne
    @JoinColumn(name = "owner_id",referencedColumnName = "id")
    private User owner;
}
