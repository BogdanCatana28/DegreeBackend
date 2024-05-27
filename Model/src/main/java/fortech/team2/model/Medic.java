package fortech.team2.model;


import fortech.team2.model.enums.MedicSpecialization;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@SuperBuilder
@Table(name = "medics")
public class Medic extends User {
    @ElementCollection(targetClass = MedicSpecialization.class)
    @CollectionTable
    @Enumerated(EnumType.STRING)
    @Column(name = "specializations")
    private List<MedicSpecialization> specializations;

    @Column(name = "experience")
    private String experience;

    @Column(name = "education")
    private String education;

    @Column(name = "days_off", columnDefinition = "integer default 25")
    private Integer daysOff;

}