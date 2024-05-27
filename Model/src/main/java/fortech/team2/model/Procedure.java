package fortech.team2.model;

import fortech.team2.model.enums.MedicSpecialization;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "procedures")
public class Procedure {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "duration")
    private int duration;

    @Column(name = "price")
    private int price;

    @ElementCollection(targetClass = MedicSpecialization.class)
    @CollectionTable
    @Enumerated(EnumType.STRING)
    @Column(name = "specializations")
    private List<MedicSpecialization> specializations;

    @Column(name = "anesthesia")
    private Boolean anesthesia;
}
